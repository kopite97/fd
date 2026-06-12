# PLAN-0016: DNA Definitions Domain Migration

## Metadata

| Field      | Value                                  |
| ---------- | -------------------------------------- |
| Plan ID    | PLAN-0016                              |
| Title      | DNA Definitions Domain Migration       |
| Type       | Refactor                               |
| Status     | Completed                              |
| Created At | 2026-06-12                             |
| Updated At | 2026-06-12                             |

---

## Goal

Move `dna_definitions` implementation ownership from `assessment` to an independent `dna` domain while preserving existing assessment and club behavior.

---

## Scope

This Plan includes:

* confirming `DECISION-0010` is accepted before implementation
* moving the DNA definition domain model to `com.kopite.fd.dna.domain.model`
* moving the DNA definition repository interface to `com.kopite.fd.dna.domain.repository`
* moving `DnaDefinitionJpaEntity` to `com.kopite.fd.dna.infrastructure.entity`
* moving `DnaDefinitionJpaRepository` to `com.kopite.fd.dna.infrastructure.repository`
* moving `DnaDefinitionPersistenceAdapter` to `com.kopite.fd.dna.infrastructure.adapter`
* preserving `dna_definitions` table mapping without schema changes
* updating imports and Spring bean wiring affected by the package move
* updating tests that reference DNA definition classes
* ensuring assessment behavior still works after the move
* ensuring club DNA score behavior still works after the move
* ensuring no `assessment` package class owns `DnaDefinition` after migration
* running required tests and checks
* executing runtime verification against a running application because the migration changes component scanning and persistence wiring

---

## Out of Scope

This Plan does not include:

* changing the `dna_definitions` database schema
* adding public DNA definition APIs
* adding admin DNA definition APIs
* implementing DNA definition writes or synchronization
* implementing DNA rubric persistence
* changing assessment scoring behavior
* changing club DNA score response contract
* changing recommendation scoring behavior
* replacing all club DNA score native projections unless required to preserve behavior
* adding new dependencies, frameworks, or architectural patterns

---

## Tasks

### Phase 1

* [x] Confirm `DECISION-0010` is accepted before implementation
* [x] Review applicable AGENTS, accepted Decisions, active Plan index, `DATABASE_SCHEMA.md`, `BACKEND_ARCHITECTURE.md`, `PACKAGE_STRUCTURE.md`, `ENTITY_CONVENTIONS.md`, and `CODING_CONVENTIONS.md`
* [x] Review `FOOTBALL_DNA_DATA_SYNC_MAPPING.md`, `FOOTBALL_DNA_DATA.md`, and `PROJECT_OVERVIEW.md`
* [x] Identify all production and test references to current assessment-owned DNA definition classes
* [x] Confirm no public DNA API is included in this Plan

### Phase 2

* [x] Create `com.kopite.fd.dna` package structure needed for the migrated classes
* [x] Move `DnaDefinition` domain model to `dna.domain.model`
* [x] Move `DnaDefinitionRepository` to `dna.domain.repository`
* [x] Move `DnaDefinitionJpaEntity` to `dna.infrastructure.entity`
* [x] Move `DnaDefinitionJpaRepository` to `dna.infrastructure.repository`
* [x] Move `DnaDefinitionPersistenceAdapter` to `dna.infrastructure.adapter`
* [x] Update package declarations and imports

### Phase 3

* [x] Update assessment code and tests to use the `dna` domain repository/model where required
* [x] Update application context tests and mocks to use the `dna` repository interface
* [x] Verify club DNA score code still does not depend on assessment DNA classes
* [x] Verify club DNA score native projection still returns metadata correctly
* [x] Remove obsolete assessment-owned DNA definition source files

### Phase 4

* [x] Add or update tests for `DnaDefinitionPersistenceAdapter`
* [x] Add or update tests proving active, non-deleted DNA definitions are read from the `dna` domain
* [x] Run `./gradlew test`
* [x] Run `./gradlew check`

### Phase 5

* [x] Execute runtime verification with a running application using `./gradlew bootRun`
* [x] Verify application startup succeeds after package migration
* [x] Verify an existing assessment endpoint still responds
* [x] Verify an existing club DNA score endpoint still responds with DNA metadata
* [x] Verify `/v3/api-docs` still responds
* [x] Clean up any temporary verification rows
* [x] Stop the running application
* [x] Record validation and runtime verification results
* [x] Update Plan status, task progress, and `docs/plans/README.md`

---

## Dependencies

Related Plans (Optional):

* PLAN-0001
* PLAN-0007
* PLAN-0014
* PLAN-0015

Related Decisions (Optional):

* DECISION-0001
* DECISION-0005
* DECISION-0008
* DECISION-0010

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

* Moved `dna_definitions` implementation ownership from `assessment` to `dna`
* Added `dna` domain model, repository, JPA entity, Spring Data repository, and persistence adapter
* Removed assessment-owned `DnaDefinition` production classes
* Updated application context mocks and assessment infrastructure tests
* Added dedicated `dna` adapter test for active, non-deleted definition reads
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Executed `./gradlew check`
* Verification result: `BUILD SUCCESSFUL`
* Executed actual `./gradlew bootRun` on port `8081`
* Runtime verification result: assessment start API returned HTTP 200, club DNA score API returned DNA metadata, OpenAPI returned HTTP 200, temporary rows were deleted, and the running application was stopped
* Status set to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: `./gradlew check`
* Unit tests: updated service tests and repository adapter tests affected by DNA definition package migration
* Integration tests: DNA definition persistence read behavior and existing club DNA score projection behavior
* Manual verification:
  * confirm `dna_definitions` implementation classes are under `com.kopite.fd.dna`
  * confirm no production `assessment` package owns `DnaDefinition`
  * confirm no club code depends on assessment DNA classes
  * confirm schema mapping for `dna_definitions` is unchanged
  * confirm no public DNA definition API was introduced

Validation result:

* `dna_definitions` implementation classes now exist only under `com.kopite.fd.dna`
* no production `assessment` package owns `DnaDefinition` classes
* `FdApplicationTests` now mocks `com.kopite.fd.dna.domain.repository.DnaDefinitionRepository`
* assessment infrastructure soft-delete test no longer owns DNA definition persistence assertions
* dedicated `dna` adapter test verifies active, non-deleted definition reads
* club DNA score code still avoids any dependency on assessment DNA classes
* schema mapping for `dna_definitions` remains unchanged
* no public DNA definition API was introduced
* `./gradlew test` passed
* `./gradlew check` passed

---

## Runtime Verification

Required because this Plan changes package ownership, Spring component scanning, repository wiring, and existing executable application behavior.

Verification steps:

1. Start the application with `./gradlew bootRun`.
2. Verify application startup succeeds.
3. Call an existing assessment endpoint that exercises normal application wiring.
4. Insert temporary verification rows for `clubs`, `dna_definitions`, and `club_dna_scores` if needed.
5. Call an existing club DNA score endpoint and verify DNA metadata is still returned.
6. Verify `/v3/api-docs` returns HTTP 200.
7. Clean up temporary verification rows.
8. Stop the running application.

Success criteria:

* application startup succeeds after the package migration
* existing assessment behavior still responds successfully
* existing club DNA score behavior still returns DNA metadata
* OpenAPI still responds
* temporary verification data is cleaned up

Verification result:

* Passed on 2026-06-12
* Executed actual `./gradlew bootRun` on port `8081`
* Verified `/v3/api-docs` returned HTTP 200 after startup
* Verified `POST /api/assessments` returned HTTP 200 with a created assessment for `anonymousId=runtime-dna-migration`
* Inserted temporary `clubs`, `dna_definitions`, and `club_dna_scores` rows
* Verified `GET /api/club-dna-scores?dataVersion=club-rtdna1` returned DNA metadata including `dnaDefinitionId`, `dnaCategory`, `dnaKey`, and `dataVersion`
* Deleted temporary `club_dna_scores`, `dna_definitions`, `clubs`, and `user_assessments` rows
* Stopped the running `bootRun` process on PID `23876`

---

## Completion Criteria

This Plan is considered complete when:

* [x] `DECISION-0010` is Accepted
* [x] All tasks are completed
* [x] `dna_definitions` implementation ownership is under `com.kopite.fd.dna`
* [x] assessment no longer owns `DnaDefinition` classes
* [x] assessment behavior remains compatible
* [x] club DNA score behavior remains compatible
* [x] no public DNA definition API is introduced
* [x] Scope requirements are satisfied
* [x] Lint or static checks pass
* [x] Related tests pass
* [x] Runtime verification executes against a running application
* [x] Runtime verification passes
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
