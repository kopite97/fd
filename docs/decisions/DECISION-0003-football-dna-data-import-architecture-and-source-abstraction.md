# DECISION-0003: Football DNA Data Import Architecture and Source Abstraction

## Metadata

| Field | Value |
| --- | --- |
| Decision ID | DECISION-0003 |
| Title | Football DNA Data Import Architecture and Source Abstraction |
| Status | Accepted |
| Created At | 2026-06-10 |
| Updated At | 2026-06-10 |

---

## Context

Football DNA Data is the source of truth for recommendation-related club data and is currently published through multiple Google Sheet CSV URLs.

The current manual import implementation was introduced to verify published CSV retrieval and preserve raw artifacts. That structure is now insufficient because the import flow must support multiple source sheets such as:

- `clubs`
- `emotionalDNA`
- `playstyleDNA`
- `clubStatus`
- `clubTags`
- `dnaRubric`

In addition, the project now requires that the import architecture be designed for future data source replacement. Future versions may replace published CSV transport with:

- Google Sheets API
- external JSON APIs
- other structured remote sources

Before expanding the import workflow, the project needs a clear architectural decision for:

- how application logic remains independent from CSV-specific behavior
- how multiple sheet or source inputs are represented
- where data acquisition, parsing, conversion, and artifact generation responsibilities belong
- how future format or schema changes are isolated from application orchestration
- how failures should be handled when importing multiple related datasets

These choices are difficult to reverse because they affect external integration structure, application-service design, testing strategy, and future synchronization behavior.

---

## Decision

### 1. Source-Independent Import Architecture

The application-layer import workflow must depend on abstractions rather than concrete CSV implementations.

Application services must not directly depend on:

- published CSV URLs
- CSV parsing details
- Google Sheets API response models
- JSON-specific payload structures

The import workflow will orchestrate a source-neutral pipeline and delegate format-specific behavior to infrastructure-backed abstractions.

### 2. Import Pipeline Separation

The import architecture will separate the workflow into four responsibilities:

1. Data acquisition
2. Data parsing
3. Domain import model conversion
4. Artifact generation

These responsibilities must remain independently replaceable.

Their intended roles are:

- Data acquisition: retrieve raw source content from a configured external source
- Data parsing: interpret raw source content according to source format such as CSV or JSON and produce a parsed representation without embedding application import workflow concerns
- Domain import model conversion: translate parsed representations into the common internal import model used by application orchestration
- Artifact generation: persist import evidence for inspection, debugging, or traceability

The boundary between parsing and conversion is explicit:

- parsing is source-format interpretation only
- conversion is application-facing translation into the internal import model

Parsing should not decide application import structure, target orchestration behavior, or persistence meaning. Conversion is the step that adapts parsed source data into the stable model consumed by the application layer.

### 3. Common Internal Import Model

All supported sources must be converted into a common internal import model before application-level processing continues.

That model must be able to represent:

- multiple import targets
- per-target record collections
- source metadata
- validation or import result metadata as needed

The common model must be expressive enough to support:

- CSV-based imports
- JSON-based imports
- future Google Sheets API integrations

This internal model is an application-level integration model.

It is not required to match:

- domain models
- persistence entities
- downstream recommendation structures

Its purpose is to provide a stable handoff boundary between external source handling and application orchestration.

### 4. Interface Boundaries

The architecture will define explicit abstraction boundaries for at least the following responsibilities:

- source acquisition
- payload parsing
- internal import model conversion
- artifact persistence

Application services may coordinate these interfaces, but must not embed source-format-specific logic.

The infrastructure layer will provide implementations for concrete transports and formats such as:

- published CSV HTTP fetch
- JSON HTTP fetch
- future Google Sheets API fetch
- CSV parsing
- JSON parsing

### 5. Configuration Strategy

For the current stage, source locations may continue to be configured per import target using application configuration and environment-backed values.

Per-target configuration is accepted for the current published-sheet setup because each required dataset is exposed through a distinct published URL.

### 6. Source Discovery Strategy

For the current stage, import targets are explicitly configured.

This means:

- the set of import targets is defined in application configuration
- each target is resolved through configured source information
- the import workflow does not attempt to discover new sources automatically at runtime

Automatic source discovery is out of scope for this Decision and for the current stage of implementation.

If the project later requires dynamic source discovery, sheet metadata lookup, registry-backed target discovery, or Google Sheets API-based source enumeration, that must be introduced through a separate Decision.

This does not prevent future replacement with:

- a Google Sheets API-backed configuration model
- a grouped source descriptor model
- a non-Google external data source registry

### 7. Failure Strategy

The import workflow will use a fail-fast overall result with per-target execution reporting.

Meaning:

- if any required import target fails, the overall import execution is treated as failed
- the result must still preserve which targets succeeded and which failed

This strategy is chosen because Football DNA recommendation data is a connected dataset and partial success should not silently appear as a complete synchronization result.

### 8. Schema Change Isolation

Source schema details such as:

- CSV column names
- JSON property names
- source-specific field arrangements

must be isolated outside the application orchestration layer.

They should be handled in parsing and conversion components, or in dedicated target-specific mapping logic.

Future schema or column changes should therefore require updates only in source-specific or target-specific conversion logic, not in the application import workflow itself.

### 9. Runtime Verification Requirement

Any Plan that changes executable Football DNA Data import behavior must include runtime verification.

Runtime verification must execute the running application and confirm:

- the configured import endpoint can be called
- configured targets are actually retrieved from their runtime source
- the resulting artifact or artifacts are generated as expected
- failure handling is observable at runtime when a target is unavailable or invalid

---

## Alternatives Considered

### Option A

Keep the application workflow directly coupled to published CSV URLs and CSV parsing.

Pros:

* smallest immediate change from the current implementation
* simple for one-sheet or one-format imports

Cons:

* does not scale cleanly to multiple targets
* tightly couples application logic to current CSV transport
* makes future migration to JSON or Google Sheets API more disruptive

### Option B

Introduce a source-independent import pipeline with explicit boundaries for acquisition, parsing, conversion, and artifact generation.

Pros:

* supports current CSV transport without locking the application to CSV
* allows future JSON or Google Sheets API implementations behind stable interfaces
* isolates schema changes from application orchestration
* provides a cleaner basis for testing and future synchronization work

Cons:

* introduces more abstraction than the smallest possible implementation
* requires additional design discipline before implementation

### Option C

Adopt Google Sheets API immediately and remove published CSV support.

Pros:

* centralizes around a richer long-term integration option
* can reduce dependence on manually managed published URLs

Cons:

* exceeds the current requirement
* introduces credentials, permissions, and new operational complexity
* does not address JSON-source extensibility by itself

---

## Consequences

Positive:

* application-level import logic is protected from current source-format coupling
* published CSV remains usable in the short term without constraining future transport changes
* future JSON and Google Sheets API integrations can be added behind stable interfaces
* target-specific schema changes can be localized to parsing or conversion components
* runtime verification becomes an explicit part of executable import work

Negative:

* the import architecture becomes more layered than a direct CSV-only workflow
* the project must define and maintain a common internal import model
* additional implementation work is required before moving on to full database synchronization

---

## Related Plans

* PLAN-0004

---

## Change History

### 2026-06-10

* Decision created
* Status set to Proposed

### 2026-06-10

* Decision approved by user
* Status set to Accepted
