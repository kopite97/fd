# PLAN-0010: String-Based Version Identifier Policy Alignment Fix

## Metadata

| Field      | Value |
| ---------- | ----------------------------------------------------------------- |
| Plan ID    | PLAN-0010 |
| Title      | String-Based Version Identifier Policy Alignment Fix |
| Type       | Fix |
| Status     | Completed |
| Created At | 2026-06-11 |
| Updated At | 2026-06-11 |

---

## Goal

Implement the accepted `DECISION-0006` string-based version identifier policy by aligning the currently implemented assessment persistence layer, related runtime behavior, and database/document consistency for:

- `question_version`
- `algorithm_version`
- `club_data_version`

This Plan focuses on removing outdated integer-based assumptions and ensuring that version identifiers are treated as stable string references rather than numeric counters.

---

## Scope

This Plan includes:

* aligning assessment-layer version identifier usage with `DECISION-0006`
* updating currently implemented assessment JPA entity mappings so `question_version`, `algorithm_version`, and `club_data_version` use `String` where applicable
* removing integer-based conversion logic for assessment version identifier fields from persistence mapping paths
* reviewing and correcting remaining assessment-related schema-document inconsistencies related to version identifier naming, defaults, and notes
* reviewing the current PostgreSQL schema state for assessment version fields against the documented `VARCHAR(20)` version identifier policy
* identifying and applying the minimum required database alignment for currently implemented assessment version fields:
  * version field length
  * version field type consistency
  * nullable/default settings that must not conflict with accepted assessment lifecycle rules
* adding or updating tests for string-based `algorithm_version` persistence behavior in the assessment flow
* running runtime verification only for currently implemented assessment flows using string-based version identifiers
* recording validation results and any explicitly deferred gaps that depend on unimplemented Club or Recommendation domain behavior

---

## Out of Scope

This Plan does not include:

* implementing the recommendation domain
* implementing the club domain
* implementing Football DNA Data database synchronization into `clubs`, `club_dna_scores`, or `club_tags`
* introducing a new recommendation algorithm
* redesigning assessment lifecycle rules beyond the accepted freeze timing in `DECISION-0001`
* changing unrelated schema fields outside version identifier policy alignment
* implementing `club_dna_scores` persistence behavior
* implementing `club_dna_scores.data_version` runtime assignment or validation behavior
* implementing `club_data_version` freeze behavior during recommendation generation
* implementing club snapshot version synchronization behavior
* implementing recommendation-domain version handling

---

## Tasks

### Phase 1

* [x] Reconfirm the accepted rules in `DECISION-0006` and the affected assessment lifecycle rules in `DECISION-0001`
* [x] Inventory all current assessment-domain code, schema, and test references to `question_version`, `algorithm_version`, and `club_data_version`
* [x] Reconfirm the live PostgreSQL schema state for the assessment version columns and document remaining gaps versus `DATABASE_SCHEMA.md`

### Phase 2

* [x] Update affected assessment JPA entities so string-based version identifier fields use `String`
* [x] Remove integer-based conversion logic from assessment persistence mapping paths for affected version identifier fields
* [x] Align column metadata such as length and nullability in the affected assessment JPA entities with the accepted lifecycle rules
* [x] Verify that existing assessment application behavior remains consistent with `DECISION-0001` and `DECISION-0006`

### Phase 3

* [x] Correct any remaining assessment-related version identifier inconsistencies in `docs/architecture/DATABASE_SCHEMA.md`
* [x] Review whether `ENTITY_CONVENTIONS.md` requires clarification for assessment-layer string-based version identifiers under `DECISION-0006`
* [x] Record the required PostgreSQL migration or schema-adjustment actions needed to align the live assessment schema with the accepted policy

### Phase 4

* [x] Add or update tests covering string-based `algorithm_version` persistence and round-trip behavior
* [x] Add or update tests for affected assessment persistence mapping behavior involving `question_version`, `algorithm_version`, and `club_data_version`
* [x] Run `./gradlew test`
* [x] Run `./gradlew check` if applicable to the project

### Phase 5

* [x] Execute runtime verification for currently implemented assessment flows using string-based version identifiers
* [x] Verify that persisted `algorithm_version` is stored and read back as a string identifier such as `alg-v1`
* [x] Verify that assessment creation, question retrieval, answer submission, and assessment completion do not fail due to integer-based version assumptions
* [x] Document the deferred verification boundary for `club_data_version` runtime freezing because recommendation generation is not yet implemented
* [x] Update task status, validation results, and final Plan status when completion criteria are satisfied

---

## Dependencies

Related Plans (Optional):

* PLAN-0001
* PLAN-0006
* PLAN-0009

Related Decisions (Optional):

* DECISION-0001
* DECISION-0004
* DECISION-0006

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

* Reconfirmed the accepted string-based version identifier rules in `DECISION-0006` and the assessment freeze timing rules in `DECISION-0001`
* Reconfirmed the live PostgreSQL state for `user_assessments.question_version`, `algorithm_version`, and `club_data_version`
* Updated `AssessmentJpaEntity` so `algorithm_version` and `club_data_version` use `String` with `VARCHAR(20)`-aligned JPA metadata
* Removed Integer-based persistence conversion logic for assessment version identifier fields
* Updated assessment tests to use string-based version identifiers such as `q-v1`, `q-v2`, and `alg-v1`
* Added a persistence round-trip test for string-based assessment version identifiers
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Executed `./gradlew check`
* Verification result: `BUILD SUCCESSFUL`
* Executed runtime verification for assessment creation, question retrieval, answer submission, and assessment completion using `q-v1` and `alg-v1`
* Runtime verification result: the assessment flow completed successfully and persisted `question_version = q-v1` and `algorithm_version = alg-v1`
* Updated `DATABASE_SCHEMA.md` so assessment version identifier rows and `club_dna_scores.data_version` match `DECISION-0006`
* Deferred gap remains limited to future `club_data_version` runtime freeze behavior because recommendation generation is not yet implemented
* Plan status set to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: project Gradle compile and verification tasks applicable to the current codebase
* Unit tests: assessment version identifier type alignment, persistence mapping behavior, and string-based `algorithm_version` round-trip behavior
* Integration tests: assessment persistence flow remains valid after JPA version field alignment
* Manual verification: inspect the affected assessment schema, entity mappings, and persisted assessment records to confirm version identifiers remain string-based and lifecycle-consistent

---

## Runtime Verification

Required because this Plan affects executable assessment API behavior and persistence-backed runtime flows.

Verification steps:

1. Start the application against the local PostgreSQL environment.
2. Create an assessment using a string-based `question_version`.
3. Submit at least one answer to the created assessment.
4. Complete the assessment using a string-based `algorithm_version` such as `alg-v1`.
5. Verify through PostgreSQL that the persisted assessment row stores the expected `question_version` and `algorithm_version` values as string identifiers.
6. Verify that question retrieval, answer submission, and assessment completion do not fail due to integer-based conversion assumptions.

Success criteria:

* No runtime conversion error occurs for `question_version`, `algorithm_version`, or `club_data_version` in the affected implemented assessment persistence path.
* Persisted `question_version` and `algorithm_version` values match the submitted string identifiers.
* The implemented assessment flow remains consistent with `DECISION-0001` freeze timing and `DECISION-0006` string identifier rules.

Verification result:

* Assessment `id=6` was created with `questionVersion = q-v1`
* Question retrieval returned the expected `q-v1` question and option set
* Answer submission succeeded for the created assessment
* Assessment completion succeeded with `algorithmVersion = alg-v1`
* PostgreSQL verification confirmed `user_assessments.id = 6` stored `question_version = q-v1`, `algorithm_version = alg-v1`, and `club_data_version = null`
* PostgreSQL verification confirmed `assessment_dna_scores` persisted the finalized score snapshot for assessment `id = 6`

---

## Completion Criteria

This Plan is considered complete when:

* [x] All tasks are completed
* [x] `question_version`, `algorithm_version`, and `club_data_version` are consistently modeled as string-based identifiers in the affected implemented assessment layers
* [x] No Integer-based persistence conversion logic remains for the affected assessment version identifier fields
* [x] Documented schema and implemented assessment entity mappings are aligned for the affected version identifier fields
* [x] Required PostgreSQL alignment actions for the currently implemented assessment tables are recorded and, where in scope, applied
* [x] Scope requirements are satisfied
* [x] Lint or static checks pass
* [x] Related tests pass
* [x] Runtime verification passes for the currently implemented assessment flow
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
