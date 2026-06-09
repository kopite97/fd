# PLAN-0001: Assessment Domain MVP Backend

## Metadata

| Field | Value |
| --- | --- |
| Plan ID | PLAN-0001 |
| Title | Assessment Domain MVP Backend |
| Type | Feature |
| Status | Completed |
| Created At | 2026-06-09 |
| Updated At | 2026-06-10 |

---

## Goal

Implement the MVP backend slice for the Assessment domain so the system can:

- start an assessment session
- serve a stable question set for that session
- accept and persist assessment answers
- derive the minimum assessment DNA required by `DECISION-0001`
- finalize persisted DNA score snapshots
- produce a completed, versioned assessment result that can be consumed by future recommendation generation

This Plan is the first implementation step toward the recommendation flow defined in `PROJECT_OVERVIEW.md` and must remain consistent with `DECISION-0001`.

---

## Scope

This Plan includes:

* implementing the Assessment domain around `user_assessments` as the aggregate root
* implementing persistence and domain behavior for:
  * `user_assessments`
  * `assessment_answers`
  * `assessment_dna_scores`
* reading reference data required by the assessment flow from:
  * `questions`
  * `question_options`
  * `option_score_mappings`
  * `dna_definitions`
* enforcing the accepted identity rule:
  * at least one of `user_id` or `anonymous_id` must be present
* enforcing the accepted lifecycle states:
  * `IN_PROGRESS`
  * `COMPLETED`
* freezing `question_version` when an assessment starts
* freezing `algorithm_version` when an assessment is completed
* leaving `club_data_version` unset during assessment creation and completion
* implementing the minimum assessment DNA derivation required to finalize and persist `assessment_dna_scores` at assessment completion
* implementing the application-layer use cases needed for:
  * assessment session creation
  * question retrieval for an active session
  * answer submission
  * assessment completion
  * DNA score finalization
* implementing API endpoints and DTOs for the MVP assessment flow
* updating `docs/plans/README.md` when this Plan is created or its status changes
* adding tests covering the accepted assessment rules and the main happy-path flow

---

## Out of Scope

This Plan does not include:

* recommendation-stage scoring
* recommendation ranking generation
* similarity calculation against clubs
* club DNA comparison
* core DNA bonus logic
* beginner adjustment logic
* club explanation generation
* AI refinement
* persistence or behavior for:
  * `assessment_recommendations`
  * `assessment_ai_adjustments`
* club-data synchronization or import pipeline
* shareable result behavior
* account-linking behavior between anonymous and authenticated assessments
* new lifecycle states beyond `IN_PROGRESS` and `COMPLETED`
* broader user/account management beyond the minimum needed to support optional `user_id`
* any scoring logic beyond assessment DNA finalization from answers
* implementation work outside the approved Assessment slice

---

## Tasks

### Phase 1

* [x] Define the `assessment` package structure consistent with the documented layered architecture
* [x] Model the `user_assessments` aggregate root and its lifecycle rules
* [x] Model assessment answer and finalized DNA score persistence within the accepted consistency boundary
* [x] Define repository interfaces in the domain layer and persistence adapters in the infrastructure layer
* [x] Update `docs/plans/README.md` to register `PLAN-0001` as Approved

### Phase 2

* [x] Implement read access for active assessment questions
* [x] Implement read access for question options
* [x] Implement read access for DNA definitions and option score mappings required for assessment DNA derivation
* [x] Ensure question retrieval respects the session's frozen `question_version`

### Phase 3

* [x] Implement assessment session creation with identity validation
* [x] Freeze `question_version` when a session starts
* [x] Implement answer submission for `IN_PROGRESS` assessments only
* [x] Implement the minimum answer-to-DNA derivation using option score mappings
* [x] Persist finalized `assessment_dna_scores` on completion
* [x] Freeze `algorithm_version` when the assessment is completed
* [x] Ensure recommendation-stage scoring is not introduced in this Plan

### Phase 4

* [x] Implement request/response DTOs for assessment APIs
* [x] Implement endpoints for:
  * starting an assessment
  * retrieving questions for an assessment
  * submitting an answer
  * completing an assessment
* [x] Validate assessment identity rules
* [x] Validate lifecycle transition rules
* [x] Validate that completed assessments cannot accept additional answers

### Phase 5

* [x] Add unit tests for domain rules and assessment DNA derivation behavior
* [x] Add integration tests for persistence and application flow
* [x] Add API-level tests for the main assessment workflow
* [x] Record validation results required for Plan completion

---

## Dependencies

Related Plans (Optional):

- None

Related Decisions (Optional):

- DECISION-0001

---

## Progress Log

### 2026-06-09

* Plan created
* Status set to Approved

### 2026-06-09

* Established the `assessment` domain package structure
* Added domain model skeletons for assessments, answers, DNA scores, questions, and options
* Added domain repository interfaces and infrastructure persistence adapters
* Added JPA entity mappings and Spring Data repositories for assessment persistence
* Implemented assessment session creation with identity validation and frozen `question_version`
* Implemented question retrieval for active assessments using the assessment's frozen `question_version`

### 2026-06-09

* Implemented answer submission restricted to `IN_PROGRESS` assessments
* Added DNA reference-data read access for `dna_definitions` and `option_score_mappings`
* Implemented assessment completion with minimum DNA finalization and persisted `assessment_dna_scores`
* Implemented API request/response DTOs and a thin assessment controller wired to the existing application services

### 2026-06-10

* Added assessment application and controller tests for session start, frozen question retrieval, answer submission, completion transition, and minimum DNA aggregation
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Verified `compileJava` and `compileTestJava` passed through the test run
* Verified 14 tests completed and all tests passed
* Plan status changed to Completed

---
## Validation

Describe how this Plan should be verified.

- Lint/static checks: project Gradle checks applicable to the codebase
- Unit tests: domain lifecycle rules, identity validation, and assessment DNA derivation behavior
- Integration tests: repository persistence for assessments, answers, and finalized DNA score snapshots; end-to-end application service flow for create, answer, and complete
- Manual verification: start an assessment, retrieve the frozen question set, submit answers, complete the assessment, and verify finalized DNA scores and frozen versions are stored as expected
- Additional validation: verify `COMPLETED` assessments cannot accept further answers; verify completion fails if required DNA score finalization cannot be completed; verify at least one of `user_id` or `anonymous_id` is present; verify `question_version` remains fixed for the session; verify `algorithm_version` is recorded at completion; verify persisted `assessment_dna_scores` match submitted answers and option mappings

---

## Completion Criteria

This Plan is considered complete when:

- [x] All tasks are completed
- [x] Assessment session creation is implemented
- [x] Question retrieval for an active assessment is implemented
- [x] Answer submission is implemented for `IN_PROGRESS` assessments
- [x] Assessment completion persists finalized `assessment_dna_scores` derived from submitted answers
- [x] Completed assessments are usable as stable inputs for later recommendation generation
- [x] `question_version` is frozen at assessment start
- [x] `algorithm_version` is frozen at assessment completion
- [x] Scope requirements are satisfied
- [x] The implementation is consistent with `DECISION-0001`
- [x] Lint or static checks pass
- [x] Related tests pass
- [x] Validation results are recorded
- [x] Plan status updated in `docs/plans/README.md`
