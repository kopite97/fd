# PLAN-0008: BaseEntity Soft Delete and Schema Convention Alignment

## Metadata

| Field      | Value |
| ---------- | ----------------------------------------------------------------- |
| Plan ID    | PLAN-0008 |
| Title      | BaseEntity Soft Delete and Schema Convention Alignment |
| Type       | Refactor |
| Status     | Completed |
| Created At | 2026-06-11 |
| Updated At | 2026-06-11 |

---

## Goal

Implement the accepted `DECISION-0005` soft delete baseline by adding `is_deleted` to the shared JPA `BaseEntity`, aligning code with the already updated schema and entity convention documents, and updating existing persistence/query behavior so normal business reads exclude logically deleted rows without changing the existing `created_at` and `updated_at` auditing behavior.

---

## Scope

This Plan includes:

* updating `global.infrastructure.entity.BaseEntity` to add the common `is_deleted` field with a default non-deleted state
* preserving the existing auditing behavior of `created_at` and `updated_at`
* aligning existing JPA entities with the new inherited `is_deleted` structure
* updating repository and adapter read behavior so normal business reads exclude logically deleted rows where applicable
* identifying and updating existing write paths that currently rely on default physical delete semantics for business data
* verifying that implementation remains aligned with the already updated `docs/architecture/DATABASE_SCHEMA.md`
* verifying that implementation remains aligned with the already updated `docs/architecture/ENTITY_CONVENTIONS.md`
* adding or updating tests that verify the new soft delete baseline and unchanged audit timestamp behavior
* running verification checks for the affected persistence and application behavior

---

## Out of Scope

This Plan does not include:

* Football DNA Data database synchronization into `clubs`, `club_dna_scores`, or `club_tags`
* new `club` domain feature implementation beyond the structural changes required by the shared soft delete baseline
* recommendation calculation or recommendation persistence implementation
* introducing a global Hibernate-specific soft delete framework if it requires a new dependency or architectural pattern
* redesigning table-specific inactive-state business rules beyond what is necessary to align with `DECISION-0005`
* physically deleting or backfilling production data outside deterministic schema/application changes in this repository

---

## Tasks

### Phase 1

* [x] Review the accepted `DECISION-0005` and the current `BaseEntity`, entity, repository, and adapter structure affected by the shared soft delete baseline
* [x] Inventory all current JPA entities and repository read paths that must align with inherited `is_deleted`
* [x] Define the exact code-level strategy for default non-deleted creation state and normal read filtering without changing the current architecture unexpectedly

### Phase 2

* [x] Implement `is_deleted` in `BaseEntity` while preserving current audit timestamp behavior
* [x] Update affected entities, repositories, and adapters so inherited soft delete state is structurally aligned and normal reads exclude logically deleted records where applicable
* [x] Update any existing delete-oriented business write paths that need explicit logical delete behavior under `DECISION-0005`

### Phase 3

* [x] Verify that the implemented `BaseEntity`, entities, repositories, and adapters remain aligned with the already updated `docs/architecture/DATABASE_SCHEMA.md`
* [x] Verify that the implemented `BaseEntity`, entities, repositories, and adapters remain aligned with the already updated `docs/architecture/ENTITY_CONVENTIONS.md`
* [x] Re-check the updated code and documents for consistency with `DECISION-0005`

### Phase 4

* [x] Add or update tests for inherited `is_deleted` behavior, default query filtering behavior, and unchanged audit timestamp behavior
* [x] Run `./gradlew test`
* [x] Run `./gradlew check` if applicable to the project

### Phase 5

* [x] Execute runtime verification against the running application for at least one affected persistence-backed read/write flow
* [x] Record runtime verification results
* [x] Update task status, validation results, and final Plan status when complete

---

## Dependencies

Related Plans (Optional):

* PLAN-0006
* PLAN-0007

Related Decisions (Optional):

* DECISION-0004
* DECISION-0005

---

## Progress Log

### 2026-06-11

* Plan created
* Status set to Proposed

### 2026-06-11

* Implementation started
* Status set to In Progress

### 2026-06-11

* Reviewed the affected JPA entities, repositories, adapters, and assessment persistence flows for inherited soft delete impact
* Implemented `is_deleted` in `BaseEntity` with a default non-deleted state while preserving existing audit timestamp behavior
* Updated assessment repository and adapter read paths so normal reads use non-deleted scope
* Added infrastructure adapter tests covering inherited soft delete defaults and non-deleted repository scope usage
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Executed `./gradlew check`
* Verification result: `BUILD SUCCESSFUL`
* Executed runtime verification against the running application for assessment creation and answer submission
* Runtime verification partial result: successful non-deleted assessment creation and answer submission on `POST /api/assessments` and `POST /api/assessments/{assessmentId}/answers`
* Runtime verification limitation: no runtime application path currently exists to mark a committed record logically deleted for end-to-end exclusion verification
* Additional runtime finding: `GET /api/assessments/{assessmentId}/questions` still fails due to a pre-existing `question_version` runtime type mismatch unrelated to this Plan

### 2026-06-11

* Registered `psql` runtime access and verified logical delete behavior directly against PostgreSQL
* Updated `user_assessments.id=2` to `is_deleted=true` and confirmed the normal assessment read path excluded the record
* Runtime verification result: `POST /api/assessments/2/answers` returned `Assessment not found.` after logical deletion
* Plan status set to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: project Gradle compile and check tasks applicable to the current codebase
* Unit tests: `BaseEntity` soft delete baseline, entity/repository filtering expectations, and unchanged audit timestamp behavior
* Integration tests: persistence-backed verification that normal reads exclude logically deleted records while explicit access paths can still retrieve them when required
* Manual verification: inspect updated schema/convention documents and confirm one affected flow in the running application behaves consistently with the new soft delete baseline

---

## Runtime Verification

Required only when the Plan introduces:

* APIs
* External integrations
* Scheduled jobs
* Data import/export flows
* Executable application behavior

Verification steps:

1. Start the application with the updated soft delete baseline.
2. Execute at least one existing persistence-backed flow that creates and reads an entity using the normal application path.
3. Verify that newly created records default to `is_deleted=false`.
4. Mark a target record as logically deleted through the implemented application or persistence path used by the Plan.
5. Verify that the normal read path no longer returns the logically deleted record while audit timestamps remain populated as before.

Success criteria:

* The application starts successfully with the new inherited `is_deleted` field.
* Newly created records default to non-deleted state.
* Normal business reads exclude logically deleted records.
* Existing `created_at` and `updated_at` behavior remains intact.

Verification result:

* Passed on 2026-06-11
* Success path:
  * Running app on port `8081`
  * `POST /api/assessments`
  * Result: assessment created successfully with `assessmentId=2`
  * `POST /api/assessments/2/answers`
  * Result: answer persisted successfully with `answerId=3`
  * `psql` update:
    * `update user_assessments set is_deleted = true where id = 2`
    * Verified `is_deleted=true` in PostgreSQL
  * `POST /api/assessments/2/answers`
  * Result: normal read path excluded the logically deleted assessment and the application returned `Assessment not found.`
* Additional unrelated runtime issue:
  * `GET /api/assessments/{assessmentId}/questions` still returned `500`
  * Cause observed at runtime: pre-existing `questions.question_version` database type mismatch with the current repository/entity expectation

---

## Completion Criteria

This Plan is considered complete when:

* [x] All tasks are completed
* [x] Scope requirements are satisfied
* [x] Lint or static checks pass
* [x] Related tests pass
* [x] Runtime verification passes (when applicable)
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
