# PLAN-0004: Football DNA Data Multi-Source Import Architecture

## Metadata

| Field | Value |
| --- | --- |
| Plan ID | PLAN-0004 |
| Title | Football DNA Data Multi-Source Import Architecture |
| Type | Feature |
| Status | Completed |
| Created At | 2026-06-10 |
| Updated At | 2026-06-10 |

---

## Goal

Redesign the Football DNA Data import workflow so that application-level import orchestration is independent from the current published CSV implementation and can support multiple explicitly configured import targets for the full Football DNA dataset.

The resulting structure must support the current published CSV URLs while remaining extensible for future JSON sources and Google Sheets API integration without requiring application-layer import logic changes.

---

## Scope

This Plan includes:

* aligning the import architecture with `DECISION-0003`
* reviewing the current `.env` and `application.yml` multi-target source configuration
* redesigning the import workflow around explicit boundaries for:
  * data acquisition
  * data parsing
  * application-level integration model conversion
  * artifact generation
* supporting explicitly configured import targets for:
  * `clubs`
  * `emotionalDNA`
  * `playstyleDNA`
  * `clubStatus`
  * `clubTags`
  * `dnaRubric`
* introducing a common internal import model that is independent from source format
* refactoring the current CSV-only import path behind abstractions consumed by the application layer
* implementing multi-target import orchestration with fail-fast overall result semantics and per-target reporting
* preserving artifact generation for runtime inspection of imported source data
* adding runtime verification for the running import API against configured sources

---

## Out of Scope

This Plan does not include:

* synchronizing imported data into database tables
* transforming imported records into final recommendation domain entities
* automatic or scheduled synchronization
* automatic source discovery
* introducing Google Sheets API as the active runtime transport in this stage
* introducing external JSON APIs as active runtime transport in this stage
* dynamic target registration at runtime
* changing recommendation scoring behavior

---

## Tasks

### Phase 1

* [x] Review the current import implementation, configuration, and accepted Decisions
* [x] Define explicit interface boundaries for acquisition, parsing, integration-model conversion, and artifact generation
* [x] Define the application-level internal import model for multi-target imports
* [x] Confirm the configured target set and target key naming strategy in application configuration

### Phase 2

* [x] Refactor application services so import orchestration depends only on abstractions
* [x] Refactor published CSV retrieval into a source-acquisition implementation behind the new interface boundary
* [x] Implement CSV parsing as a format-specific concern separate from integration-model conversion
* [x] Implement target-aware conversion from parsed CSV data into the common internal import model

### Phase 3

* [x] Implement multi-target import orchestration for all configured Football DNA targets
* [x] Implement per-target execution results and fail-fast overall import result handling
* [x] Define and implement the artifact storage strategy for multi-target imports, including whether artifacts are stored as one aggregated artifact or multiple target-specific artifacts
* [x] Update artifact generation so runtime output captures multi-target import evidence
* [x] Ensure schema or column changes remain isolated from application orchestration logic

### Phase 4

* [x] Add unit tests for source acquisition abstraction usage, parsing boundaries, and integration-model conversion
* [x] Add API tests for the multi-target import endpoint response structure
* [x] Add failure-path tests for missing configuration and target import failure handling
* [x] Run `./gradlew test`

### Phase 5

* [x] Execute runtime verification against the running application and configured sources
* [x] Record runtime verification results
* [x] Update task status, validation results, and final Plan status when complete

---

## Dependencies

Related Plans (Optional):

* PLAN-0003

Related Decisions (Optional):

* DECISION-0002
* DECISION-0003

---

## Progress Log

### 2026-06-10

* Plan created
* Status set to Proposed

### 2026-06-10

* Plan approved by user
* Status set to In Progress

### 2026-06-10

* Refactored the Football DNA import flow into explicit acquisition, parsing, integration-model conversion, and artifact-generation boundaries
* Replaced the single-URL import workflow with explicit multi-target orchestration for six configured Football DNA sources
* Adopted a single aggregated artifact strategy per import execution under `src/main/resources/data`
* Updated published CSV acquisition to follow Google redirect responses correctly
* Added unit tests for provider, parser, converter, service orchestration, and controller response structure
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Runtime verification success path executed against `http://localhost:8082/api/admin/football-dna-data/import`
* Runtime verification success result: overall success with 6 processed targets and generated artifact `src/main/resources/data/football-dna-data-import-20260610-152902.md`
* Runtime verification failure path executed against `http://localhost:8085/api/admin/football-dna-data/import` with `club-status-url` overridden to `http://127.0.0.1:9/club-status.csv`
* Runtime verification failure result: fail-fast overall failure with 4 processed targets and target-level failure visibility in generated artifact `src/main/resources/data/football-dna-data-import-20260610-153350.md`
* Status set to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: project Gradle compile and test checks applicable to the codebase
* Unit tests: acquisition abstraction orchestration, parsing isolation, integration-model conversion, per-target result handling, and failure semantics
* Integration tests: import service behavior using configured targets with source adapters mocked or stubbed as appropriate
* Manual verification: inspect generated artifact output to confirm multi-target import evidence is recorded under `src/main/resources/data`

---

## Runtime Verification

Required only when the Plan introduces:

* APIs
* External integrations
* Scheduled jobs
* Data import/export flows
* Executable application behavior

Verification steps:

1. Start the application with the configured `.env` and `application.yml` values.
2. Call the manual Football DNA Data import API against the running application.
3. Verify that targets are processed according to the selected fail-fast execution strategy and that the runtime response reports observable per-target execution results for the targets reached before overall termination.
4. Verify that import artifacts are generated for the multi-target run under `src/main/resources/data`.
5. Execute at least one failure-path runtime check by making one configured target unavailable or invalid in a controlled way, and verify that the overall result fails while per-target reporting remains visible.

Success criteria:

* The running import endpoint responds successfully when all configured targets are reachable.
* The response includes observable per-target results consistent with the selected fail-fast execution strategy.
* Artifact output for the runtime import run is present and non-empty.
* The controlled failure-path check produces an observable failed overall result with target-level failure visibility.

Verification result:

* Passed on 2026-06-10
* Success path:
  * Running app on port `8082`
  * `POST /api/admin/football-dna-data/import`
  * Result: `success=true`, `processedTargetCount=6`
  * Observed target results for `clubs`, `emotional-dna`, `playstyle-dna`, `club-status`, `club-tags`, and `dna-rubric`
  * Observed aggregated artifact at `src/main/resources/data/football-dna-data-import-20260610-152902.md`
* Failure path:
  * Running app on port `8085`
  * `club-status-url` overridden to `http://127.0.0.1:9/club-status.csv`
  * `POST /api/admin/football-dna-data/import`
  * Result: `success=false`, `processedTargetCount=4`
  * Observed fail-fast termination at `club-status` with per-target results preserved for prior successful targets
  * Observed aggregated artifact at `src/main/resources/data/football-dna-data-import-20260610-153350.md`

---

## Completion Criteria

This Plan is considered complete when:

* [x] All tasks are completed
* [x] The application-layer import workflow depends on abstractions rather than concrete CSV implementations
* [x] Multiple explicitly configured Football DNA import targets are supported by one import orchestration flow
* [x] Parsing and integration-model conversion responsibilities are separated
* [x] The common internal import model is used as the stable boundary into application orchestration
* [x] Source acquisition implementations can be replaced without modifying application-level orchestration logic
* [x] Application services do not depend on source-format-specific models or parsing structures
* [x] Schema or column changes are isolated from application orchestration logic
* [x] Scope requirements are satisfied
* [x] Lint or static checks pass
* [x] Related tests pass
* [x] Runtime verification passes (when applicable)
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
