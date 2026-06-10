# DECISION-0002: Football DNA Data Manual Import Transport and Preservation

## Metadata

| Field | Value |
| --- | --- |
| Decision ID | DECISION-0002 |
| Title | Football DNA Data Manual Import Transport and Preservation |
| Status | Accepted |
| Created At | 2026-06-10 |
| Updated At | 2026-06-10 |

---

## Context

Football DNA Data is maintained in Google Sheets and documented as the source of truth for recommendation-related club data in `docs/resources/FOOTBALL_DNA_DATA.md`.

The current implementation does not yet include any import path for that external dataset.

Before implementing a manual import API, the project needs a clear decision for:

- what transport mechanism will be used to retrieve the published Google Sheet
- whether the configuration value should be treated as a published URL or a spreadsheet identifier
- whether the first import step should normalize data into the database or preserve the raw source first
- whether the import entry point should be treated as a manual administrative operation or a domain-level end-user workflow

These choices affect external integration design, configuration semantics, testability, and the shape of later club-data synchronization work.

---

## Decision

### 1. Transport Mechanism

The system will retrieve Football DNA Data from a Google Sheet published to the web as CSV.

For this stage, the import flow will use a direct HTTP fetch against the published CSV URL rather than the authenticated Google Sheets API.

Reason:

- the current user requirement explicitly states that the sheet is published as CSV
- a published CSV URL is sufficient for the current verification-oriented scope
- it avoids introducing authentication, credentials, or Google API client dependencies before they are necessary

### 2. Configuration Semantics

The import configuration will use `google.sheets.spreadsheet-url` as the application property key.

`SPREADSHEET_URL` will be treated as the published CSV source URL for the manual import flow.

For the current project setup, the source value is maintained in the project-root `.env` file and provided to the application through that environment variable name.

This decision applies only to the current manual import scope and may be refined later if the project adopts a dedicated spreadsheet identifier plus sheet metadata model.

### 3. Import Output Strategy

The first import implementation will not transform or synchronize the CSV into normalized database records.

Instead, the import flow will preserve the fetched raw CSV as a Markdown artifact under `src/main/resources/data`.

Reason:

- the immediate goal is to verify that external retrieval works correctly
- raw source preservation provides traceability for manual inspection
- it keeps the first implementation small and focused
- it avoids prematurely locking the project into a normalization strategy before downstream mapping rules are decided

### 4. Execution Model

The import will be exposed through a manual API intended for explicit execution.

This import is treated as an administrative or operational workflow rather than an end-user feature.

The current decision does not introduce scheduled synchronization, automatic refresh, or incremental update behavior.

---

## Alternatives Considered

### Option A

Use the authenticated Google Sheets API from the start and import structured sheet data directly.

Pros:

* supports richer metadata and sheet selection behavior
* aligns better with future structured synchronization needs

Cons:

* introduces credentials and external client setup immediately
* exceeds the current verification-only requirement
* increases implementation and testing complexity

### Option B

Use a published CSV URL as the current transport and preserve the raw source before any database transformation.

Pros:

* matches the current published-sheet setup
* keeps the first integration small and testable
* preserves the original imported source for inspection
* avoids premature database mapping decisions

Cons:

* depends on the stability of a published web URL
* does not yet provide normalized application data
* the current property naming may be semantically imprecise

### Option C

Skip file preservation and import directly from CSV into database tables in the first iteration.

Pros:

* moves faster toward club-data synchronization
* avoids generating intermediate artifacts

Cons:

* makes debugging source-data issues harder
* forces transformation decisions earlier than necessary
* broadens scope beyond raw import verification

---

## Consequences

Positive:

* the project can verify Google Sheet retrieval without adding Google API credentials
* the first import path remains narrowly scoped and deterministic
* the imported source is preserved for inspection and debugging
* later normalization work can build on a proven transport step
* the property key now matches the actual meaning of the configured value

Negative:

* raw artifact storage adds an intermediate step before usable recommendation data exists
* a later Decision may still be needed for full synchronization into `clubs`, `club_dna_scores`, and `club_tags`
* runtime execution still depends on the `SPREADSHEET_URL` value being loaded into the application environment from `.env`

---

## Related Plans

* PLAN-0003

---

## Change History

### 2026-06-10

* Decision created
* Status set to Proposed

### 2026-06-10

* Decision approved by user
* Status set to Accepted
