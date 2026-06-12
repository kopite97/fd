# PLAN-0014: Club DNA Scores Domain and Query API

## Metadata

| Field      | Value                                      |
| ---------- | ------------------------------------------ |
| Plan ID    | PLAN-0014                                  |
| Title      | Club DNA Scores Domain and Query API       |
| Type       | Feature                                    |
| Status     | Completed                                  |
| Created At | 2026-06-12                                 |
| Updated At | 2026-06-12                                 |

---

## Goal

Implement the `club_dna_scores` domain and expose deterministic read APIs for club DNA score snapshot data.

The implementation will allow clients and future recommendation services to retrieve club DNA scores by data version without implementing the recommendation algorithm itself.

---

## Scope

This Plan includes:

* implementing a `ClubDnaScore` domain model aligned with `DATABASE_SCHEMA.md`
* implementing score range validation for the documented `1.00` to `5.00` MVP scale
* implementing a Club Domain repository interface for `club_dna_scores` read access
* implementing `ClubDnaScoreJpaEntity` aligned with `club_dna_scores`
* implementing a Spring Data JPA repository and persistence adapter for `club_dna_scores`
* implementing query support that remains compatible with future `dna` domain ownership of `dna_definitions`
* exposing DNA definition metadata only through an approved `dna` read abstraction or a read-only persistence projection that does not depend on `assessment` package types
* implementing application query/result models and read-only services
* implementing public read APIs for:
  * DNA scores for a specific club and data version
  * all club DNA scores for a data version
* implementing response DTOs
* exposing Swagger documentation for the new endpoints
* adding unit, integration, controller/API, and runtime verification tests
* verifying deterministic sorting
* verifying normal reads exclude logically deleted score rows
* preserving the existing `Club` aggregate boundary

---

## Out of Scope

This Plan does not include:

* creating or modifying club DNA score write APIs
* implementing Google Sheet synchronization for `emotional-dna` or `playstyle-dna`
* defining how `is_core` is sourced or derived during synchronization
* changing the `club_dna_scores` database schema
* moving `dna_definitions` implementation into a new `dna` package
* adding new Club Domain dependencies on current `assessment` package DNA implementation
* implementing recommendation similarity calculation
* implementing core DNA bonus calculation
* implementing beginner adjustment
* implementing TOP5 or TOP3 recommendation generation
* implementing AI refinement
* changing score scale from `1-5` to `0-100`
* changing existing `Club` domain model to contain DNA score collections
* adding a new dependency, framework, or architectural pattern

---

## Tasks

### Phase 1

* [x] Confirm `DECISION-0008` is accepted before implementation
* [x] Review applicable AGENTS, accepted Decisions, active Plan index, `DATABASE_SCHEMA.md`, `BACKEND_ARCHITECTURE.md`, `PACKAGE_STRUCTURE.md`, `ENTITY_CONVENTIONS.md`, `API_CONVENTIONS.md`, and `CODING_CONVENTIONS.md`
* [x] Review `FOOTBALL_DNA_DATA_SYNC_MAPPING.md`, `FOOTBALL_DNA_DATA.md`, and `PROJECT_OVERVIEW.md`
* [x] Review existing `club` and `club_tags` implementation structure
* [x] Confirm the implementation does not depend on current `assessment` package DNA definition classes
* [x] Confirm endpoint paths, required query parameters, response fields, and sorting before coding

### Phase 2

* [x] Implement `ClubDnaScore` domain model
* [x] Implement score range validation for `1.00` to `5.00`
* [x] Implement `ClubDnaScoreRepository` domain repository interface
* [x] Implement application query models for club/data-version and all/data-version lookups
* [x] Implement application result models with `dnaDefinitionId` and, where safely available, DNA definition metadata

### Phase 3

* [x] Implement `ClubDnaScoreJpaEntity`
* [x] Implement `ClubDnaScoreJpaRepository`
* [x] Implement `ClubDnaScorePersistenceAdapter`
* [x] Implement DNA metadata lookup through a future-compatible `dna` read abstraction or a read-only persistence projection without using `assessment` package types
* [x] Ensure repository reads filter `is_deleted = false`
* [x] Ensure repository reads require exact `data_version` matching
* [x] Ensure repository reads apply deterministic sorting

### Phase 4

* [x] Implement read-only application services for specific-club DNA scores and all scores by version
* [x] Implement `ClubDnaScoreController`
* [x] Implement response DTOs
* [x] Add Swagger annotations for the new endpoints

### Phase 5

* [x] Add tests for domain validation and application service behavior
* [x] Add persistence or integration tests for repository filtering, version matching, metadata, and sorting
* [x] Add controller/API tests for endpoint wiring and response structure
* [x] Run `./gradlew test`
* [x] Run `./gradlew check`

### Phase 6

* [x] Execute runtime verification with a running application using `./gradlew bootRun`
* [x] Insert temporary verification rows for `clubs`, `dna_definitions`, and `club_dna_scores`
* [x] Call the specific-club DNA scores endpoint and verify response data
* [x] Call the all-club DNA scores endpoint and verify response data
* [x] Verify Swagger/OpenAPI exposes the new club DNA score endpoints
* [x] Clean up temporary verification rows
* [x] Stop the running application
* [x] Record validation and runtime verification results
* [x] Update Plan status, task progress, and `docs/plans/README.md`

---

## Dependencies

Related Plans (Optional):

* PLAN-0011
* PLAN-0012
* PLAN-0013

Related Decisions (Optional):

* DECISION-0005
* DECISION-0006
* DECISION-0007
* DECISION-0008

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

* Implemented `club_dna_scores` domain model, repository interface, JPA entity, Spring Data repository, and persistence adapter
* Implemented read-only services for specific-club and all-score lookup by exact `dataVersion`
* Implemented `ClubDnaScoreController` with `GET /api/clubs/{clubId}/dna-scores` and `GET /api/club-dna-scores`
* Added response DTOs and Swagger annotations
* Implemented DNA metadata lookup through a read-only native projection over `dna_definitions` without using `assessment` package types
* Added domain, service, controller, repository integration, and runtime verification tests
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Executed `./gradlew check`
* Verification result: `BUILD SUCCESSFUL`
* Executed actual `./gradlew bootRun` on port `8081`
* Runtime verification result: both club DNA score APIs returned HTTP 200 with inserted temporary score data, OpenAPI exposed both endpoints, logically deleted score row was excluded, temporary rows were deleted, and the running application was stopped
* Status set to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: `./gradlew check`
* Unit tests: domain score range validation and application service behavior
* Integration tests: JPA repository or persistence adapter filtering, exact `dataVersion` matching, DNA metadata exposure, and deterministic sorting
* API tests: endpoint wiring, required query parameter handling, response structure, and status code behavior
* Manual verification:
  * confirm package ownership remains under `com.kopite.fd.club`
  * confirm `Club` does not gain a DNA score collection in this Plan
  * confirm `ClubDnaScoreJpaEntity` aligns with `DATABASE_SCHEMA.md`
  * confirm `dataVersion` is a `String` and exact-match query input
  * confirm Club Domain code does not depend on current `assessment` package DNA definition classes
  * confirm any DNA metadata lookup remains replaceable by the future `dna` domain
  * confirm Swagger/OpenAPI exposes the new endpoints

Validation result:

* `ClubDnaScore` domain model and repository interface implemented under `com.kopite.fd.club`
* `ClubDnaScoreJpaEntity` maps `club_dna_scores.club_id` and `club_dna_scores.dna_definition_id` as scalar IDs according to `DECISION-0008`
* score range validation enforces `1.00` to `5.00`
* repository queries filter `club_dna_scores.is_deleted = false`
* repository queries require exact `dataVersion` matching
* repository queries expose DNA metadata through a read-only native projection over `dna_definitions`
* Club Domain code does not depend on current `assessment` package DNA definition classes
* specific-club lookup is sorted by DNA category order, DNA display order, and score row ID
* all-score lookup is sorted by club ID, DNA category order, DNA display order, and score row ID
* `Club` domain model was not changed to contain DNA score collections
* `./gradlew test` passed
* `./gradlew check` passed

---

## Runtime Verification

Required because this Plan introduces public API behavior.

Verification steps:

1. Start the application with `./gradlew bootRun`.
2. Insert temporary verification data into `clubs`, `dna_definitions`, and `club_dna_scores`.
3. Call `GET /api/clubs/{clubId}/dna-scores?dataVersion=club-v1`.
4. Call `GET /api/club-dna-scores?dataVersion=club-v1`.
5. Verify responses include the inserted score, `dnaDefinitionId`, score, `isCore`, and `dataVersion`.
6. If DNA metadata lookup is implemented in the approved scope, verify responses include DNA category and DNA key without using `assessment` package types.
7. Verify logically deleted score rows are excluded.
8. Verify `/v3/api-docs` contains the new endpoints.
9. Delete the temporary verification data.
10. Stop the running application.

Success criteria:

* application startup succeeds with the new entity, repository, services, and controller registered
* specific-club DNA score lookup returns HTTP 200 and expected response data
* all-score lookup returns HTTP 200 and expected response data
* deleted rows are excluded from normal API responses
* OpenAPI includes the new endpoints
* temporary verification rows are cleaned up

Verification result:

* Passed on 2026-06-12
* Executed actual `./gradlew bootRun` on port `8081`
* Inserted temporary verification data with club code `HTTP_DNA_VERIFY_01`, DNA key `runtime_http_dna_verify`, and data version `club-http-v1`
* Inserted one normal club DNA score row and one logically deleted score row
* Verified `GET /api/clubs/39/dna-scores?dataVersion=club-http-v1` returned HTTP 200 with the normal score row
* Verified `GET /api/club-dna-scores?dataVersion=club-http-v1` returned HTTP 200 with the normal score row
* Verified response included `dnaDefinitionId`, `dnaCategory`, `dnaKey`, `score`, `core`, and `dataVersion`
* Verified the logically deleted score row was excluded from API responses
* Verified `/v3/api-docs` returned HTTP 200 and included `/api/clubs/{clubId}/dna-scores` and `/api/club-dna-scores`
* Deleted the temporary verification rows and stopped the `bootRun` process

---

## Completion Criteria

This Plan is considered complete when:

* [x] `DECISION-0008` is Accepted
* [x] All tasks are completed
* [x] `club_dna_scores` domain model, repository interface, JPA entity, JPA repository, and adapter exist
* [x] Score range validation follows the approved `1.00` to `5.00` policy
* [x] Read-only application services exist for specific-club and all-score lookup by data version
* [x] Public read APIs and response DTOs exist
* [x] Swagger/OpenAPI exposes the new endpoints
* [x] `club_dna_scores` reads filter logically deleted rows
* [x] `club_dna_scores` reads require exact `dataVersion` matching
* [x] `club_dna_scores` reads are deterministically sorted
* [x] Scope requirements are satisfied
* [x] Lint or static checks pass
* [x] Related tests pass
* [x] Runtime verification executes against a running application
* [x] Runtime verification passes
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
