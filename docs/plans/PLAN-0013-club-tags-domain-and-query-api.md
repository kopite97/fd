# PLAN-0013: Club Tags Domain and Query API

## Metadata

| Field      | Value                               |
| ---------- | ----------------------------------- |
| Plan ID    | PLAN-0013                           |
| Title      | Club Tags Domain and Query API      |
| Type       | Feature                             |
| Status     | Completed                           |
| Created At | 2026-06-12                          |
| Updated At | 2026-06-12                          |

---

## Goal

Implement the `club_tags` domain and expose deterministic read APIs for club tag data.

The implementation will allow clients to retrieve tags for a specific club and retrieve all active club tags while preserving the existing Club Domain architecture.

---

## Scope

This Plan includes:

* implementing a `ClubTag` domain model aligned with `DATABASE_SCHEMA.md`
* implementing a Club Domain repository interface for `club_tags` read access
* implementing `ClubTagJpaEntity` aligned with `club_tags`
* implementing a Spring Data JPA repository and persistence adapter for `club_tags`
* implementing application query/result models and read-only services
* implementing public read APIs for:
  * tags for a specific club
  * all active club tags
* implementing response DTOs
* exposing Swagger documentation for the new endpoints
* adding unit, integration, or controller tests for the read behavior
* verifying deterministic sorting by `display_order` and `id`
* verifying normal reads exclude inactive and logically deleted tags
* reviewing existing `Club` implementation and preserving its current aggregate boundary

---

## Out of Scope

This Plan does not include:

* creating or modifying club tag write APIs
* implementing Google Sheet synchronization for `club-tags`
* defining `tag_type` classification or derivation policy
* changing existing `Club` domain model to contain tag collections
* implementing `club_dna_scores`
* implementing recommendation logic
* implementing Team DNA page behavior
* implementing AI explanation generation
* changing database schema
* adding a new dependency, framework, or architectural pattern

---

## Tasks

### Phase 1

* [x] Confirm `DECISION-0007` is accepted before implementation
* [x] Review applicable AGENTS, `DATABASE_SCHEMA.md`, `BACKEND_ARCHITECTURE.md`, `PACKAGE_STRUCTURE.md`, `ENTITY_CONVENTIONS.md`, `API_CONVENTIONS.md`, and `CODING_CONVENTIONS.md`
* [x] Review existing `club` domain, infrastructure, application, and test structure
* [x] Confirm endpoint paths and response fields before coding

### Phase 2

* [x] Implement `ClubTag` domain model
* [x] Implement `ClubTagRepository` domain repository interface
* [x] Implement application query models for tag lookups
* [x] Implement application result models for tag responses

### Phase 3

* [x] Implement `ClubTagJpaEntity`
* [x] Implement `ClubTagJpaRepository`
* [x] Implement `ClubTagPersistenceAdapter`
* [x] Ensure repository reads filter `is_active = true` and `is_deleted = false`
* [x] Ensure repository reads apply deterministic sorting

### Phase 4

* [x] Implement read-only application services for specific-club tags and all tags
* [x] Implement `ClubTagController`
* [x] Implement response DTOs
* [x] Add Swagger annotations for the new endpoints

### Phase 5

* [x] Add tests for application service behavior
* [x] Add persistence or integration tests for repository filtering and sorting
* [x] Add controller/API tests for endpoint wiring and response structure
* [x] Run `./gradlew test`
* [x] Run `./gradlew check`

### Phase 6

* [x] Execute runtime verification against the running application context
* [x] Verify Swagger/OpenAPI exposes the new club tag endpoints
* [x] Record validation and runtime verification results
* [x] Update Plan status, task progress, and `docs/plans/README.md`

---

## Dependencies

Related Plans (Optional):

* PLAN-0011
* PLAN-0012

Related Decisions (Optional):

* DECISION-0005
* DECISION-0007

---

## Progress Log

### 2026-06-12

* Plan created
* Status set to Proposed

### 2026-06-12

* Plan approved by user
* Status set to Approved

### 2026-06-12

* Implementation started
* Status set to In Progress

### 2026-06-12

* Implemented `club_tags` domain model, repository interface, JPA entity, Spring Data repository, and persistence adapter
* Implemented read-only application services for specific-club and all-tag lookup
* Implemented `ClubTagController` with `GET /api/clubs/{clubId}/tags` and `GET /api/club-tags`
* Added response DTOs and Swagger annotations
* Added service, controller, repository integration, and runtime verification tests
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Executed `./gradlew check`
* Verification result: `BUILD SUCCESSFUL`
* Runtime verification result: running application context served both club tag APIs and `/v3/api-docs` included both new endpoints
* Status set to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: `./gradlew check`
* Unit tests: application services for specific-club and all-tag lookup behavior
* Integration tests: JPA repository or persistence adapter filtering and deterministic sorting
* API tests: endpoint wiring, response structure, and status code behavior
* Manual verification:
  * confirm package ownership remains under `com.kopite.fd.club`
  * confirm `Club` does not gain a tag collection in this Plan
  * confirm `ClubTagJpaEntity` aligns with `DATABASE_SCHEMA.md`
  * confirm Swagger/OpenAPI exposes the new endpoints

Validation result:

* `ClubTag` domain model and repository interface implemented under `com.kopite.fd.club`
* `ClubTagJpaEntity` maps `club_tags.club_id` as scalar `Long clubId` according to `DECISION-0007`
* repository queries filter `is_active = true` and `is_deleted = false`
* specific-club lookup is sorted by `display_order ASC, id ASC`
* all-tag lookup is sorted by `club_id ASC, display_order ASC, id ASC`
* `Club` domain model was not changed to contain tag collections
* `./gradlew test` passed
* `./gradlew check` passed

---

## Runtime Verification

Required because this Plan introduces public API behavior.

Verification steps:

1. Start the application against the configured database.
2. Call the specific-club tags endpoint.
3. Call the all-club-tags endpoint.
4. Verify responses include only active, non-deleted tags.
5. Verify response ordering is deterministic.
6. Verify the generated OpenAPI document contains the new endpoints.

Success criteria:

* application startup succeeds with the new entity, repository, services, and controller registered
* specific-club tag lookup returns the expected response shape and ordering
* all-tag lookup returns the expected response shape and ordering
* inactive and logically deleted tags are excluded from normal API responses
* Swagger/OpenAPI includes the new endpoints

Verification result:

* Passed on 2026-06-12
* Executed through `ClubTagApiRuntimeVerificationTest`
* Running Spring application context persisted a club tag and served `GET /api/clubs/{clubId}/tags`
* Running Spring application context served `GET /api/club-tags`
* `/v3/api-docs` included `/api/clubs/{clubId}/tags` and `/api/club-tags`
* Executed actual `./gradlew bootRun` on port `8081`
* Inserted temporary verification data with club code `HTTP_TAG_VERIFY_01`
* Verified `GET /api/clubs/26/tags` returned HTTP 200 with tag `Runtime HTTP tag`
* Verified `GET /api/club-tags` returned HTTP 200 with tag `Runtime HTTP tag`
* Verified `/v3/api-docs` returned HTTP 200 and included both club tag endpoints
* Deleted the temporary verification rows and stopped the `bootRun` process

---

## Completion Criteria

This Plan is considered complete when:

* [x] `DECISION-0007` is Accepted
* [x] All tasks are completed
* [x] `club_tags` domain model, repository interface, JPA entity, JPA repository, and adapter exist
* [x] Read-only application services exist for specific-club and all-tag lookup
* [x] Public read APIs and response DTOs exist
* [x] Swagger/OpenAPI exposes the new endpoints
* [x] `club_tags` reads filter inactive and logically deleted rows
* [x] `club_tags` reads are deterministically sorted
* [x] Scope requirements are satisfied
* [x] Lint or static checks pass
* [x] Related tests pass
* [x] Runtime verification passes
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
