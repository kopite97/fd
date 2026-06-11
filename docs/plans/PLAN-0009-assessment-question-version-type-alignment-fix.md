# PLAN-0009: Assessment Question Version Type Alignment Fix

## Metadata

| Field      | Value |
| ---------- | ----------------------------------------------------------------- |
| Plan ID    | PLAN-0009 |
| Title      | Assessment Question Version Type Alignment Fix |
| Type       | Fix |
| Status     | Completed |
| Created At | 2026-06-11 |
| Updated At | 2026-06-11 |

---

## Goal

Resolve the `question_version` type mismatch by aligning the Database Schema, PostgreSQL, domain, JPA, repository, and assessment persistence layers to the current string-based version identifier model so assessment question retrieval works correctly at runtime.

---

## Scope

This Plan includes:

* reviewing all assessment-layer definitions and usages of `question_version`
* aligning `question_version` with the current source-of-truth model:
  * `VARCHAR(20)`
  * default `v1`
  * string version identifier semantics
* updating assessment JPA entities so `questionVersion` uses `String`
* updating affected repository query signatures so `questionVersion` uses `String`
* removing integer conversion logic from persistence adapters related to `question_version`
* preserving existing assessment business behavior while fixing the runtime query mismatch
* reviewing whether any database migration is required for the current PostgreSQL state
* updating or adding tests affected by the type alignment
* executing runtime verification that assessment question retrieval works correctly after the fix
* documenting the validation outcome and impact scope in the Plan

---

## Out of Scope

This Plan does not include:

* redesigning assessment versioning policy beyond the already accepted current string identifier model
* changing `algorithm_version` or `club_data_version` semantics unless required by the same proven type-alignment issue
* recommendation domain implementation
* club domain implementation
* Football DNA Data database synchronization
* unrelated assessment refactoring outside the `question_version` alignment problem
* introducing a new Decision unless implementation reveals a broader design conflict not currently evidenced

---

## Tasks

### Phase 1

* [x] Reconfirm the current source-of-truth definition for `question_version` across the latest schema and PostgreSQL state
* [x] Inventory all affected files in domain, DTO, entity, repository, adapter, service, and test layers
* [x] Confirm whether the current issue is resolvable as a local type-alignment fix without requiring a new Decision

### Phase 2

* [x] Update affected JPA entities so `questionVersion` is modeled as `String`
* [x] Update affected repository methods and persistence adapters to remove integer-based query and conversion logic
* [x] Verify that assessment application and controller flows remain behaviorally unchanged except for the fixed runtime query behavior

### Phase 3

* [x] Review whether any database migration is required for the current PostgreSQL schema and data state
* [x] If migration is not required, record the reason in the Plan validation results
* [x] If an unexpected schema/data conflict is discovered, stop and identify whether a follow-up Plan or Decision is required

### Phase 4

* [x] Update or add tests affected by the `question_version` type alignment
* [x] Run `./gradlew test`
* [x] Run `./gradlew check` if applicable to the project

### Phase 5

* [x] Execute runtime verification against the running application for assessment start and question retrieval using string-based `question_version` values such as `v1`
* [x] Confirm that runtime question retrieval no longer fails with the PostgreSQL varchar-versus-integer mismatch
* [x] Record runtime verification results and finalize Plan status when completion criteria are satisfied

---

## Dependencies

Related Plans (Optional):

* PLAN-0001
* PLAN-0006
* PLAN-0008

Related Decisions (Optional):

* DECISION-0001
* DECISION-0004

---

## Progress Log

### 2026-06-11

* Plan created
* Status set to Proposed

### 2026-06-11

* Plan approved by user
* Implementation started
* Status set to In Progress

### 2026-06-11

* Reconfirmed that `question_version` is defined as `VARCHAR(20)` in the current schema and as `character varying` in PostgreSQL
* Identified that the runtime mismatch was isolated to the assessment JPA entity, assessment question JPA entity, repository signature, and persistence adapter integer conversion logic
* Implemented string-based `question_version` alignment in the affected JPA and repository layers without introducing a new Decision
* Determined that database migration is not required because PostgreSQL already stores `question_version` as `character varying`
* Updated the affected infrastructure adapter test coverage for string-based question version lookup
* Cleared corrupted Gradle test result artifacts and re-ran verification successfully
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Executed `./gradlew check`
* Verification result: `BUILD SUCCESSFUL`
* Executed runtime verification against the running application and PostgreSQL database
* Runtime verification result: assessment creation with `questionVersion=v1` succeeded and question retrieval returned `200 OK` with `questionVersion=v1` and no varchar-versus-integer PostgreSQL error
* Plan status set to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: project Gradle compile and check tasks applicable to the current codebase
* Unit tests: assessment type alignment behavior and adapter/repository interactions for string-based `question_version`
* Integration tests: assessment persistence and service flows using string values such as `v1` and `v2`
* Manual verification: start an assessment with a string `questionVersion`, retrieve questions for that assessment, and confirm the request succeeds without database type mismatch
* Database migration evaluation: verify the current PostgreSQL column type for `user_assessments.question_version` and `questions.question_version`; if both are already `character varying`, record that no migration is required

---

## Runtime Verification

Required only when the Plan introduces:

* APIs
* External integrations
* Scheduled jobs
* Data import/export flows
* Executable application behavior

Verification steps:

1. Start the application with the current PostgreSQL database.
2. Create an assessment using a string `questionVersion` such as `v1`.
3. Call `GET /api/assessments/{assessmentId}/questions`.
4. Verify that retrieved questions belong to the same frozen question_version.
5. Confirm that the query executes successfully against PostgreSQL without a varchar-versus-integer comparison failure.
6. Confirm that the returned response is consistent with the frozen string `questionVersion`.

Success criteria:

* The application starts successfully after the type alignment changes.
* Assessment creation with a string `questionVersion` succeeds.
* Assessment question retrieval succeeds at runtime.
* No PostgreSQL type mismatch error occurs for `question_version`.

Verification result:

* Passed on 2026-06-11
* Success path:
  * Running app on port `8081`
  * `POST /api/assessments`
  * Request body used `questionVersion=\"v1\"`
  * Result: assessment created successfully with `assessmentId=3`
  * PostgreSQL verification:
    * `user_assessments.question_version` stored as `v1`
    * `questions.question_version` column type confirmed as `character varying`
  * `GET /api/assessments/3/questions`
  * Result: `200 OK`
  * Response contained `questionVersion=\"v1\"` and an empty `questions` list
* Confirmed outcome:
  * No PostgreSQL varchar-versus-integer comparison error occurred during runtime question retrieval

---

## Completion Criteria

This Plan is considered complete when:

* [x] All tasks are completed
* [x] Scope requirements are satisfied
* [x] `question_version` is consistently modeled as `String` across the affected assessment layers
* [x] No Integer-based conversion logic remains for question_version
* [x] Database migration need has been explicitly evaluated and recorded
* [x] Lint or static checks pass
* [x] Related tests pass
* [x] Runtime verification passes (when applicable)
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
