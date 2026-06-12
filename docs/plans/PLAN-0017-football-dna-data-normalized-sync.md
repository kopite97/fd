# PLAN-0017: Football DNA Data Normalized Synchronization

## Metadata

| Field      | Value                                             |
| ---------- | ------------------------------------------------- |
| Plan ID    | PLAN-0017                                         |
| Title      | Football DNA Data Normalized Synchronization      |
| Type       | Feature                                           |
| Status     | Completed                                        |
| Created At | 2026-06-12                                        |
| Updated At | 2026-06-12                                        |

---

## Goal

Implement a manual administrator-triggered normalized synchronization workflow that loads Football DNA Data through the existing import pipeline and updates the database tables used by club and recommendation features.

---

## Scope

This Plan includes:

* confirming `DECISION-0011` is accepted before implementation
* using the existing Published CSV import abstraction as the MVP source loader
* synchronizing `clubs`
* synchronizing `club_tags`
* synchronizing `club_dna_scores`
* excluding `dna_definitions` from write synchronization
* excluding assessment and recommendation result tables from synchronization
* implementing normalization from imported source records into database write models
* implementing source gap fallback behavior defined by `DECISION-0011`
* validating cross-sheet club key consistency
* validating DNA key coverage against active, non-deleted `dna_definitions`
* validating DNA score range
* validating status normalization
* requiring an explicit request `dataVersion`
* failing if `club_dna_scores` rows already exist for the requested `dataVersion`
* treating `dataVersion` as an immutable Club DNA Dataset Snapshot identifier
* rejecting overwrite, partial update, row append, or partial reuse of an existing `dataVersion`
* applying database writes in one transaction after validation succeeds
* exposing a manual admin API:
  * `POST /api/admin/football-dna-data/sync`
* protecting the manual sync API with a temporary admin access restriction until project authentication exists
* allowing the initial implementation to use an environment-configured admin sync token/header, such as `X-Admin-Sync-Token`, as the temporary restriction
* returning a structured synchronization result
* adding unit, integration, controller/API, and runtime verification tests
* verifying synchronized database state directly during runtime verification, not only API responses
* recording validation and runtime verification results

---

## Out of Scope

This Plan does not include:

* introducing Google Sheets API as the active transport
* adding new external dependencies
* automatic startup synchronization
* scheduled synchronization
* dynamic source discovery
* synchronizing `dna_definitions`
* synchronizing `dna-rubric`
* adding a sync run history table
* adding a latest-version registry table
* changing existing database schema
* changing recommendation scoring behavior
* changing public club, tag, or DNA score API contracts
* implementing admin UI
* implementing full authentication or role-based authorization
* exposing the sync endpoint as a public unauthenticated API

---

## Plan Size Assessment

`PLAN-0017` intentionally covers the synchronization engine, validation, database writes, admin execution API, runtime verification, and temporary operational restriction in one vertical slice.

Current recommendation: keep this Plan as one implementation unit for MVP.

Reason:

* the normalized sync behavior is only useful when source loading, validation, transactional writes, and runtime execution work together
* validation without writes would not prove database synchronization behavior
* writes without the admin execution path would not satisfy the operational requirement
* runtime verification cannot be meaningful until the complete sync path exists
* splitting the work too early would create intermediate Plans that are hard to verify independently and could increase coordination overhead

If implementation proves too large, the fallback split boundary is:

* `PLAN-0017A`: normalization and validation service with no database writes or admin API
* `PLAN-0017B`: transactional database synchronization for `clubs`, `club_tags`, and `club_dna_scores`
* `PLAN-0017C`: admin execution API, temporary admin access restriction, and runtime verification

The Plan does not need to be split before implementation unless the implementation becomes difficult to review or verify in one change set.

---

## Tasks

### Phase 1

* [x] Confirm `DECISION-0011` is accepted before implementation
* [x] Review applicable AGENTS, accepted Decisions, active Plan index, `DATABASE_SCHEMA.md`, `BACKEND_ARCHITECTURE.md`, `PACKAGE_STRUCTURE.md`, `ENTITY_CONVENTIONS.md`, `API_CONVENTIONS.md`, and `CODING_CONVENTIONS.md`
* [x] Review `FOOTBALL_DNA_DATA_SYNC_MAPPING.md`, `FOOTBALL_DNA_DATA.md`, and `PROJECT_OVERVIEW.md`
* [x] Review existing import pipeline and admin data import package structure
* [x] Review existing club, club tag, club DNA score, and DNA definition implementations
* [x] Confirm endpoint path, request body, response fields, validation behavior, transaction boundary, and temporary admin access restriction before coding

### Phase 2

* [x] Define synchronization request, command, result, and validation error models
* [x] Define normalized source models for clubs, tags, and DNA scores
* [x] Implement target record lookup helpers for imported target data
* [x] Implement required source column validation
* [x] Implement status normalization
* [x] Implement tag normalization
* [x] Define `club_tags` restore identity as exactly `club_id` plus normalized `tag_name`
* [x] Ensure `display_order`, `tag_type`, and `is_active` are not part of `club_tags` restore identity and are updated values after restore
* [x] Implement emotional and playstyle wide-to-row DNA score normalization
* [x] Implement source gap fallback values defined by `DECISION-0011`
* [x] Implement synchronized-scope tracking so source removal only applies to in-scope records

### Phase 3

* [x] Implement validation for club key uniqueness
* [x] Implement validation for cross-sheet club key references
* [x] Implement validation for DNA key coverage through the `dna` domain
* [x] Implement validation for score range
* [x] Implement validation that requested `dataVersion` does not already exist
* [x] Implement validation that existing `dataVersion` snapshots are never overwritten, partially updated, appended to, or partially reused
* [x] Ensure validation failure produces no database writes

### Phase 4

* [x] Implement repository or adapter operations needed for synchronized writes
* [x] Implement `clubs` upsert by `code`
* [x] Implement `clubs` soft-delete for source-removed clubs
* [x] Implement `club_tags` current set replacement for synchronized clubs
* [x] Implement `club_tags` restore behavior for matching soft-deleted rows using the exact key `club_id` plus normalized `tag_name`
* [x] Ensure the same normalized `tag_name` under a different `club_id` is not treated as the same tag
* [x] Ensure source-removal soft-delete applies only to `clubs` and `club_tags` inside the synchronization scope
* [x] Ensure sync never modifies `dna_definitions`, assessment, user, recommendation, or AI tables
* [x] Implement `club_dna_scores` snapshot insertion for requested `dataVersion`
* [x] Ensure database writes occur inside one transaction after validation succeeds

### Phase 5

* [x] Implement synchronization application service
* [x] Implement `POST /api/admin/football-dna-data/sync`
* [x] Implement the selected temporary admin access restriction before sync execution
* [x] If the initial implementation uses a token/header, document the selected header and configuration explicitly in code, tests, and runtime notes
* [x] Reject sync requests when the temporary admin access restriction is missing, invalid, or not configured
* [x] Implement request and response DTOs
* [x] Add Swagger annotations for the new admin endpoint
* [x] Ensure existing import endpoint behavior remains unchanged

### Phase 6

* [x] Add unit tests for normalization and fallback behavior
* [x] Add unit tests for soft-deleted `club_tags` restore behavior
* [x] Add unit tests proving `club_tags` restore matching uses `club_id` plus normalized `tag_name`
* [x] Add unit tests proving the same normalized `tag_name` for different clubs does not match
* [x] Add unit tests for source-removal scope protection
* [x] Add unit tests for validation failures
* [x] Add unit tests for immutable `dataVersion` rejection rules
* [x] Add application service tests for success and failure flows
* [x] Add integration tests for transactional write behavior
* [x] Add integration tests proving out-of-scope tables are not modified by sync
* [x] Add integration tests verifying database row counts and requested `dataVersion` after successful sync
* [x] Add controller/API tests for request validation and response structure
* [x] Add controller/API tests for missing, invalid, and valid temporary admin access restriction behavior
* [x] Add regression tests for existing club read APIs after sync
* [x] Run `./gradlew test`
* [x] Run `./gradlew check`

### Phase 7

* [x] Execute runtime verification with a running application using `./gradlew bootRun`
* [x] Call `POST /api/admin/football-dna-data/sync` without satisfying the temporary admin access restriction and verify the request is rejected
* [x] Call `POST /api/admin/football-dna-data/sync` with an invalid temporary admin access restriction value and verify the request is rejected
* [x] Call `POST /api/admin/football-dna-data/sync` with a valid temporary admin access restriction value and a unique `dataVersion`
* [x] Verify synchronized `clubs` data is queryable through `GET /api/clubs`
* [x] Verify synchronized `club_tags` data is queryable through `GET /api/club-tags`
* [x] Verify synchronized `club_dna_scores` data is queryable through `GET /api/club-dna-scores?dataVersion=...`
* [x] Query the database directly after sync and verify `clubs` row count for synchronized data
* [x] Query the database directly after sync and verify `club_tags` row count for synchronized data
* [x] Query the database directly after sync and verify `club_dna_scores` row count for the requested `dataVersion`
* [x] Query the database directly after sync and verify the requested `dataVersion` was persisted on synchronized `club_dna_scores` rows
* [x] Verify rerunning sync with the same `dataVersion` fails without overwriting, partially updating, appending to, or reusing that snapshot
* [x] Verify the duplicate-version attempt does not change direct database row counts or existing snapshot rows
* [x] Verify `/v3/api-docs` exposes the new sync endpoint
* [x] Stop the running application
* [x] Record validation and runtime verification results
* [x] Update Plan status, task progress, and `docs/plans/README.md`

---

## Dependencies

Related Plans (Optional):

* PLAN-0004
* PLAN-0011
* PLAN-0012
* PLAN-0013
* PLAN-0014
* PLAN-0015
* PLAN-0016

Related Decisions (Optional):

* DECISION-0003
* DECISION-0005
* DECISION-0006
* DECISION-0007
* DECISION-0008
* DECISION-0010
* DECISION-0011

---

## Progress Log

### 2026-06-12

* Plan created
* Status set to Proposed

### 2026-06-12

* Status changed to Approved after user approval

### 2026-06-12

* Status changed to In Progress

### 2026-06-12

* Implemented normalized synchronization API and persistence workflow
* Added application, integration, controller, and regression tests
* Executed `./gradlew test`
* Executed `./gradlew check`
* Executed runtime verification with running application
* Status changed to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: `./gradlew check`
* Unit tests: normalization, fallback values, validation errors, and sync result construction
* Integration tests: transactional writes for `clubs`, `club_tags`, and `club_dna_scores`
* API tests: manual sync endpoint request validation, response structure, and temporary admin access restriction
* Manual verification:
  * confirm source loading reuses existing Published CSV import abstraction
  * confirm no Google Sheets API dependency was introduced
  * confirm `dna_definitions` is read for key resolution but not synchronized
  * confirm sync does not write assessment or recommendation tables
  * confirm sync does not modify user or AI tables
  * confirm source-removal decisions are limited to synchronization-scope data
  * confirm soft-deleted `club_tags` are restored only when `club_id` and normalized `tag_name` both match
  * confirm `club_dna_scores` requires a new explicit immutable `dataVersion`
  * confirm existing `dataVersion` snapshots cannot be overwritten, partially updated, appended to, or partially reused
  * confirm the sync endpoint rejects unauthenticated requests through the temporary admin access restriction
  * confirm direct database row counts for `clubs`, `club_tags`, and `club_dna_scores` after sync
  * confirm the requested `dataVersion` is persisted in `club_dna_scores`
  * confirm rollback occurs on validation or write failure

Validation result:

* Passed on 2026-06-12
* `./gradlew test` passed
* `./gradlew check` passed

---

## Runtime Verification

Required because this Plan introduces an executable admin API, external source loading, and database synchronization behavior.

Runtime verification must validate both API behavior and actual database state. API responses alone are not sufficient for this Plan.

Verification steps:

1. Start the application with `./gradlew bootRun`.
2. Call `POST /api/admin/football-dna-data/sync` without satisfying the temporary admin access restriction.
3. Verify the request is rejected.
4. Call `POST /api/admin/football-dna-data/sync` with an invalid temporary admin access restriction value.
5. Verify the request is rejected.
6. Call `POST /api/admin/football-dna-data/sync` with a valid temporary admin access restriction value and a unique `dataVersion`.
7. Verify the sync response reports success and table-level counts.
8. Call `GET /api/clubs` and verify synchronized clubs are visible according to public read policy.
9. Call `GET /api/club-tags` and verify synchronized tags are visible.
10. Call `GET /api/club-dna-scores?dataVersion=<dataVersion>` and verify synchronized DNA scores are visible.
11. Query the database directly and verify the expected `clubs` row count for synchronized data.
12. Query the database directly and verify the expected `club_tags` row count for synchronized data.
13. Query the database directly and verify the expected `club_dna_scores` row count for the requested `dataVersion`.
14. Query the database directly and verify synchronized `club_dna_scores` rows contain the requested `dataVersion`.
15. Call `POST /api/admin/football-dna-data/sync` again with the same `dataVersion`.
16. Verify the second sync fails because the immutable snapshot version already exists.
17. Verify the duplicate-version attempt does not overwrite, partially update, append to, or partially reuse the existing snapshot.
18. Query the database directly and verify the duplicate-version attempt did not change row counts or existing snapshot rows.
19. Verify `/v3/api-docs` includes `/api/admin/football-dna-data/sync`.
20. Stop the running application.

Success criteria:

* application startup succeeds
* sync endpoint rejects missing or invalid temporary admin access restriction requests
* manual sync endpoint executes against configured source data
* synchronized club, tag, and DNA score data is queryable through existing read APIs
* direct database checks confirm expected `clubs`, `club_tags`, and `club_dna_scores` row counts
* direct database checks confirm the requested `dataVersion` is stored in synchronized `club_dna_scores`
* duplicate `dataVersion` is rejected without changing the existing immutable snapshot
* OpenAPI exposes the sync endpoint
* verification result is recorded in this Plan

Verification result:

* Passed on 2026-06-12
* Application started through `./gradlew bootRun` with `ADMIN_FOOTBALL_DNA_SYNC_TOKEN=codex-sync-token`
* Missing temporary admin access restriction request returned HTTP 403
* Invalid temporary admin access restriction request returned HTTP 403
* Valid sync request for `club-codex-001` returned HTTP 200 and `success=true`
* Sync result counts: `clubs=20`, `club_tags=80`, `club_dna_scores=280`
* Read APIs returned HTTP 200 for `GET /api/clubs`, `GET /api/club-tags`, and `GET /api/club-dna-scores?dataVersion=club-codex-001`
* Direct database verification returned `clubs=20`, `club_tags=80`, `club_dna_scores=280`, and one requested `dataVersion`
* Duplicate sync for `club-codex-001` returned `success=false` and kept `club_dna_scores` row count at 280
* `/v3/api-docs` exposed `/api/admin/football-dna-data/sync`
* Running application was stopped

---

## Completion Criteria

This Plan is considered complete when:

* [x] `DECISION-0011` is Accepted
* [x] All tasks are completed
* [x] Manual normalized sync API exists
* [x] Published CSV source loading path is reused
* [x] `clubs`, `club_tags`, and `club_dna_scores` are synchronized
* [x] `dna_definitions` is not written by sync
* [x] assessment and recommendation tables are not written by sync
* [x] user and AI tables are not written by sync
* [x] source-removal decisions are limited to synchronization-scope records
* [x] soft-deleted `club_tags` are restored only when `club_id` and normalized `tag_name` both match
* [x] validation failures produce no database writes
* [x] database writes occur in one transaction
* [x] sync endpoint is protected by a temporary admin access restriction
* [x] missing or invalid temporary admin access restriction requests are rejected
* [x] duplicate `dataVersion` sync is rejected
* [x] existing `dataVersion` snapshots cannot be overwritten, partially updated, appended to, or partially reused
* [x] Scope requirements are satisfied
* [x] Lint or static checks pass
* [x] Related tests pass
* [x] Runtime verification executes against a running application
* [x] Runtime verification includes direct database row count and requested `dataVersion` checks
* [x] Runtime verification passes
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
