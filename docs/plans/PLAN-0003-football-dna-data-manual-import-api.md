# PLAN-0003: Football DNA Data Manual Import API

## Metadata

| Field | Value |
| --- | --- |
| Plan ID | PLAN-0003 |
| Title | Football DNA Data Manual Import API |
| Type | Feature |
| Status | In Progress |
| Created At | 2026-06-10 |
| Updated At | 2026-06-10 |

---

## Goal

Implement a manual API that retrieves the published Football DNA Data Google Sheet as CSV using the configured `SPREADSHEET_URL`, verifies that the import succeeds, and persists the imported raw CSV as a Markdown artifact under `src/main/resources/data`.

This Plan is intentionally limited to raw import verification and source preservation. It does not yet transform the CSV into normalized club or recommendation database records.

---

## Scope

This Plan includes:

* reviewing `docs/resources/FOOTBALL_DNA_DATA.md` as the source description for the imported dataset
* reading the Google Sheet publication URL from application configuration
* implementing an application use case that manually fetches the published CSV from the configured URL
* implementing an API endpoint that triggers the manual import
* validating the minimum configuration required to run the import
* creating `src/main/resources/data` when it does not already exist
* writing the imported raw CSV into a Markdown file for inspection and traceability
* returning an API response that confirms whether the import and file generation succeeded
* adding tests for the manual import flow and failure handling

---

## Out of Scope

This Plan does not include:

* transforming CSV rows into database entities
* synchronizing `clubs`, `club_dna_scores`, or `club_tags`
* introducing scheduled or automatic synchronization
* handling incremental sync, deduplication, or version reconciliation
* changing recommendation logic or assessment behavior
* introducing Google Sheets authenticated APIs if the published CSV URL is sufficient
* adding operational access control beyond the current project baseline

---

## Tasks

### Phase 1

* [x] Review the Football DNA Data resource and confirm the manual import scope against the published CSV transport
* [x] Define the import entry point package structure consistent with `AGENTS.md` and `PACKAGE_STRUCTURE.md`
* [x] Confirm the configuration binding strategy for `SPREADSHEET_URL`

### Phase 2

* [x] Implement configuration binding for the Google Sheet import URL
* [x] Implement infrastructure code to fetch the published CSV content
* [x] Implement file output logic that creates `src/main/resources/data` when missing
* [x] Implement Markdown generation that preserves the raw imported CSV for inspection

### Phase 3

* [x] Implement the application service that orchestrates manual import and artifact generation
* [x] Implement the API endpoint and response DTO for manual import execution
* [x] Validate failure cases such as missing configuration, empty response, fetch failure, and file write failure

### Phase 4

* [x] Add unit and API-level tests for successful manual import
* [x] Add tests for configuration and transport failure scenarios
* [x] Run `./gradlew test`
* [x] Record validation results and update Plan status when complete

---

## Dependencies

Related Plans (Optional):

- None

Related Decisions (Optional):

- DECISION-0002

---

## Progress Log

### 2026-06-10

* Plan created
* Status set to Proposed

### 2026-06-10

* Plan approved by user
* Status set to In Progress

### 2026-06-10

* Added `.env` import support through Spring configuration loading
* Implemented manual admin API for published Football DNA Data CSV import
* Implemented published CSV fetch and Markdown artifact persistence under `src/main/resources/data`
* Added application-service and controller tests for success and failure scenarios
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Verified 18 tests completed and all tests passed
* Status set to Completed

---
## Validation

Describe how this Plan should be verified.

- Lint/static checks: project Gradle compile and test checks applicable to the codebase
- Unit tests: configuration validation, CSV fetch orchestration, Markdown artifact generation, and failure handling
- Integration tests: application-service-level import flow with mocked transport and filesystem interaction where appropriate
- API tests: manual import endpoint success and failure responses
- Manual verification: call the import endpoint, confirm a Markdown artifact is created under `src/main/resources/data`, and verify that the stored content reflects the fetched CSV source

---

## Completion Criteria

This Plan is considered complete when:

- [x] All tasks are completed
- [x] A manual API exists that fetches the published Football DNA Data CSV using configured application settings
- [x] The imported raw CSV is written as a Markdown artifact under `src/main/resources/data`
- [x] Required directories are created automatically when absent
- [x] Failure conditions are handled with explicit validation or error responses
- [x] Scope requirements are satisfied
- [x] Lint or static checks pass
- [x] Related tests pass
- [x] Validation results are recorded
- [x] Plan status updated in `docs/plans/README.md`
