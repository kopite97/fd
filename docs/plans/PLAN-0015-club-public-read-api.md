# PLAN-0015: Club Public Read API

## Metadata

| Field      | Value                     |
| ---------- | ------------------------- |
| Plan ID    | PLAN-0015                 |
| Title      | Club Public Read API      |
| Type       | Feature                   |
| Status     | Completed                 |
| Created At | 2026-06-12                |
| Updated At | 2026-06-12                |

---

## Goal

Implement public read APIs for the `clubs` table so clients can retrieve club lists and club detail data for recommendation results, Team DNA pages, and club detail screens.

---

## Scope

This Plan includes:

* implementing public `clubs` list lookup
* implementing public single-club detail lookup by `clubId`
* exposing only active, non-deleted clubs according to `DECISION-0009`
* implementing pageable list responses for `GET /api/clubs`
* using deterministic default sorting by `id` ascending
* implementing separate summary and detail response DTOs
* implementing application query/result models and read-only services
* extending the Club Domain repository interface with public read operations
* implementing persistence adapter and Spring Data JPA query support
* implementing Swagger documentation for the new endpoints
* adding unit, integration, controller/API, and runtime verification coverage
* verifying the API with a running application using `./gradlew bootRun`

---

## Out of Scope

This Plan does not include:

* club create, update, delete, or admin APIs
* exposing inactive clubs through public APIs
* exposing logically deleted clubs through public APIs
* code-based club detail lookup
* adding a `display_order` or source-order column to `clubs`
* changing the `clubs` database schema
* embedding `club_tags` in the club detail response
* embedding `club_dna_scores` in the club detail response
* creating a composite Team DNA read model
* implementing recommendation generation
* implementing Football DNA Data synchronization
* adding a new dependency, framework, or architectural pattern

---

## Tasks

### Phase 1

* [x] Confirm `DECISION-0009` is accepted before implementation
* [x] Review applicable AGENTS, accepted Decisions, active Plan index, `DATABASE_SCHEMA.md`, `BACKEND_ARCHITECTURE.md`, `PACKAGE_STRUCTURE.md`, `ENTITY_CONVENTIONS.md`, `API_CONVENTIONS.md`, and `CODING_CONVENTIONS.md`
* [x] Review `FOOTBALL_DNA_DATA_SYNC_MAPPING.md`, `FOOTBALL_DNA_DATA.md`, and `PROJECT_OVERVIEW.md`
* [x] Review existing `club`, `club_tags`, and `club_dna_scores` implementation structure
* [x] Confirm endpoint paths, response fields, filtering, pagination, and sorting before coding

### Phase 2

* [x] Extend the Club Domain repository interface with public read methods
* [x] Implement application query models for list and detail lookup
* [x] Implement application result models for summary and detail responses
* [x] Implement read-only application services for list and detail lookup

### Phase 3

* [x] Implement Spring Data JPA repository methods for active, non-deleted club reads
* [x] Implement persistence adapter methods for pageable list lookup
* [x] Implement persistence adapter methods for detail lookup by `clubId`
* [x] Ensure list reads filter `is_active = true` and `is_deleted = false`
* [x] Ensure detail reads filter `is_active = true` and `is_deleted = false`
* [x] Ensure list reads apply deterministic `id` ascending sorting by default

### Phase 4

* [x] Implement `ClubController`
* [x] Implement `ClubSummaryResponse`
* [x] Implement `ClubDetailResponse`
* [x] Implement pageable list response DTO
* [x] Return `404 Not Found` for hidden, deleted, or non-existent club detail lookups
* [x] Add Swagger annotations for the new endpoints

### Phase 5

* [x] Add application service tests for list and detail behavior
* [x] Add persistence or integration tests for active/deleted filtering, pagination, and sorting
* [x] Add controller/API tests for endpoint wiring, response structure, pagination parameters, and 404 behavior
* [x] Run `./gradlew test`
* [x] Run `./gradlew check`

### Phase 6

* [x] Execute runtime verification with a running application using `./gradlew bootRun`
* [x] Insert temporary verification rows for active, inactive, and deleted clubs
* [x] Call `GET /api/clubs` and verify only active, non-deleted clubs are returned
* [x] Call `GET /api/clubs/{clubId}` for an active club and verify detail response data
* [x] Call `GET /api/clubs/{clubId}` for inactive, deleted, and missing clubs and verify `404 Not Found`
* [x] Verify Swagger/OpenAPI exposes the new club endpoints
* [x] Clean up temporary verification rows
* [x] Stop the running application
* [x] Record validation and runtime verification results
* [x] Update Plan status, task progress, and `docs/plans/README.md`

---

## Dependencies

Related Plans (Optional):

* PLAN-0012
* PLAN-0013
* PLAN-0014

Related Decisions (Optional):

* DECISION-0005
* DECISION-0009

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

* Implemented `GET /api/clubs` and `GET /api/clubs/{clubId}`
* Implemented public active/non-deleted club filtering, pageable list response, and deterministic `id` ascending sorting
* Implemented separate summary and detail DTOs without audit, active, or deleted state exposure
* Added application service, controller/API, persistence integration, and runtime context tests
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Executed `./gradlew check`
* Verification result: `BUILD SUCCESSFUL`
* Executed actual `./gradlew bootRun` on port `8081`
* Runtime verification result: list and detail APIs returned expected HTTP responses, inactive/deleted/missing club detail lookups returned `404`, OpenAPI exposed the new endpoints, temporary rows were deleted, and the running application was stopped
* Status set to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: `./gradlew check`
* Unit tests: application service behavior for list and detail lookup
* Integration tests: repository or persistence adapter filtering, pagination, and deterministic sorting
* API tests: endpoint wiring, response structure, pagination parameters, and `404 Not Found` behavior
* Manual verification:
  * confirm implementation remains under `com.kopite.fd.club`
  * confirm controllers do not access infrastructure directly
  * confirm DTOs do not expose JPA entities, domain models, audit timestamps, or deleted state
  * confirm public reads exclude inactive and deleted clubs
  * confirm Swagger/OpenAPI exposes the new endpoints

Validation result:

* `ClubRepository` exposes public read operations without depending on Spring Data `Page`
* `ClubPersistenceAdapter` uses Spring Data pagination and `id` ascending sorting only in infrastructure
* public list and detail reads filter `is_active = true` and `is_deleted = false`
* list response uses `ClubSummaryResponse`
* detail response uses `ClubDetailResponse`
* public DTOs do not expose JPA entities, domain models, audit timestamps, active state, or deleted state
* detail lookup for hidden, deleted, and missing clubs returns `404 Not Found`
* Swagger/OpenAPI exposes `/api/clubs` and `/api/clubs/{clubId}`
* `./gradlew test` passed
* `./gradlew check` passed

---

## Runtime Verification

Required because this Plan introduces public API behavior.

Verification steps:

1. Start the application with `./gradlew bootRun`.
2. Insert temporary verification data into `clubs`, including active, inactive, and logically deleted rows.
3. Call `GET /api/clubs?page=0&size=20`.
4. Verify the list response includes the active club and excludes inactive and logically deleted clubs.
5. Call `GET /api/clubs/{clubId}` for the active club.
6. Verify the detail response contains expected public club fields.
7. Call `GET /api/clubs/{clubId}` for inactive, deleted, and missing club IDs.
8. Verify each hidden or missing detail lookup returns `404 Not Found`.
9. Verify `/v3/api-docs` contains `/api/clubs` and `/api/clubs/{clubId}`.
10. Delete the temporary verification data.
11. Stop the running application.

Success criteria:

* application startup succeeds with the new controller, services, repository methods, and DTOs registered
* list lookup returns HTTP 200 and expected pageable response data
* list lookup excludes inactive and logically deleted clubs
* detail lookup for an active club returns HTTP 200 and expected response data
* detail lookup for inactive, deleted, and missing clubs returns HTTP 404
* OpenAPI includes the new endpoints
* temporary verification rows are cleaned up

Verification result:

* Passed on 2026-06-12
* Executed actual `./gradlew bootRun` on port `8081`
* Verified `/v3/api-docs` returned HTTP 200 after startup
* Inserted temporary clubs with codes `RTPUBACT01`, `RTPUBINA01`, and `RTPUBDEL01`
* Verified `GET /api/clubs?page=0&size=100` included `RTPUBACT01`
* Verified `GET /api/clubs?page=0&size=100` excluded `RTPUBINA01` and `RTPUBDEL01`
* Verified `GET /api/clubs/58` returned HTTP 200 with expected detail fields
* Verified `GET /api/clubs/59`, `GET /api/clubs/60`, and `GET /api/clubs/999999999` returned HTTP 404
* Verified `/v3/api-docs` contained `/api/clubs` and `/api/clubs/{clubId}`
* Deleted temporary verification rows
* Stopped the running `bootRun` process on PID `9100`

---

## Completion Criteria

This Plan is considered complete when:

* [x] `DECISION-0009` is Accepted
* [x] All tasks are completed
* [x] Public club list API exists
* [x] Public club detail API exists
* [x] Public reads filter inactive and logically deleted clubs
* [x] List API is pageable
* [x] List API is deterministically sorted by `id` ascending by default
* [x] Separate summary and detail DTOs exist
* [x] Swagger/OpenAPI exposes the new endpoints
* [x] Scope requirements are satisfied
* [x] Lint or static checks pass
* [x] Related tests pass
* [x] Runtime verification executes against a running application
* [x] Runtime verification passes
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
