# DECISION-0011: Football DNA Data Normalized Synchronization Policy

## Metadata

| Field         | Value                                                   |
|---------------|---------------------------------------------------------|
| Decision ID   | DECISION-0011                                           |
| Title         | Football DNA Data Normalized Synchronization Policy     |
| Status        | Accepted                                                |
| Superseded By |                                                         |
| Created At    | 2026-06-12                                              |
| Updated At    | 2026-06-12                                              |

---

## Context

Football DNA Data is the source of truth for recommendation-related club data.

The project already has:

* a source-independent import pipeline for configured Google Sheet targets
* published CSV transport for the current MVP
* normalized database tables for `clubs`, `club_tags`, and `club_dna_scores`
* a completed sheet-to-schema mapping document
* implemented read APIs for club, club tags, and club DNA scores

The current import flow retrieves and preserves source data but does not synchronize normalized database tables.

Before implementing synchronization, the project needs a policy for:

* source loading
* synchronization scope
* normalization and fallback behavior
* upsert, snapshot, and deletion behavior
* version assignment
* transaction boundaries
* runtime execution
* failure handling and validation

These choices affect production data consistency, recommendation reproducibility, and future operations.

---

## Decision

Implement normalized synchronization as a manual administrator-triggered workflow built on top of the existing Football DNA Data import pipeline.

### 1. Source Loading

Use the existing configured Published CSV source loading path for MVP synchronization.

Rules:

* keep using explicitly configured source targets from `google.sheets.*-url`
* keep using the existing source acquisition, CSV parsing, and import model conversion abstractions
* do not introduce Google Sheets API credentials or SDKs in the initial synchronization implementation
* preserve the ability to replace Published CSV with Google Sheets API later through the existing import abstraction

Google Sheets API adoption requires a separate Decision or explicit Plan update.

### 2. Synchronization Scope

Initial normalized synchronization includes:

* `clubs`
* `club_tags`
* `club_dna_scores`

Initial normalized synchronization excludes:

* `dna_definitions`
* `users`
* `user_assessments`
* `questions`
* `question_options`
* `option_score_mappings`
* `assessment_answers`
* `assessment_dna_scores`
* `assessment_recommendations`
* `assessment_ai_adjustments`
* `dna-rubric`

`dna_definitions` remains separately managed by the `dna` domain.

The `dna-rubric` source sheet remains reference-only until a rubric schema exists.

### 3. Synchronization Strategy

Use a validated, transactional, normalized synchronization strategy.

High-level flow:

1. Load all required source targets.
2. Convert source records into the common import model.
3. Normalize source records into synchronization models.
4. Validate the full normalized dataset.
5. Apply database writes in one transaction.
6. Return a sync result with counts, data version, and validation status.

Write strategy by table:

* `clubs`: upsert by `clubs.code`
* `club_tags`: replace the current tag set for synchronized clubs
* `club_dna_scores`: insert a new immutable snapshot for the requested `dataVersion`

`club_dna_scores` synchronization must fail if rows already exist for the requested `dataVersion`.

Reason:

* `dataVersion` is a reproducibility boundary
* replacing an existing version could change historical recommendation inputs
* the current schema has a unique constraint on `club_id`, `dna_definition_id`, and `data_version`

### 4. Deletion Policy

Use soft-delete semantics for source removals from current master-data tables.

Rules:

* only records owned by the current synchronization scope are considered for source-removal decisions
* source-removed `clubs` within the synchronized club code set are marked `is_deleted = true`
* source-removed `club_tags` for synchronized clubs are marked `is_deleted = true`
* previous `club_dna_scores` versions are retained
* `club_dna_scores` rows are not deleted just because a club is absent from a later source version
* records outside the current synchronization scope must not be modified

Hard delete is not used for synchronized business data in the initial implementation.

Scope details:

* synchronization may only evaluate removal for `clubs`, `club_tags`, and `club_dna_scores`
* synchronization must not soft-delete or update `dna_definitions`
* synchronization must not soft-delete or update assessment, user, recommendation, or AI tables
* source sheets that are explicitly excluded from synchronization cannot trigger deletion in any table

`club_tags` re-synchronization behavior:

* tag matching is based on the synchronized club and normalized `tag_name`
* when a previously soft-deleted tag appears again in the source for the same club and normalized tag name, restore the existing row instead of creating a new row
* restored tag rows must update `display_order`, `is_active`, and `tag_type` according to current synchronization rules
* when no matching tag row exists, create a new row
* when an existing tag row for a synchronized club is absent from the current source, mark it `is_deleted = true`

Reason:

* `club_tags` has no stable external row identifier
* normalized tag name is the only deterministic source-level identity available in MVP
* restoring a matching soft-deleted row avoids unnecessary duplicate tag history for the same club/tag identity

### 5. Versioning Policy

`club_dna_scores.data_version` follows `DECISION-0006`.

Rules:

* `dataVersion` is supplied explicitly in the manual synchronization request
* example: `club-v1`
* comparison is exact equality only
* no numeric ordering is allowed
* previous versions are retained
* synchronization fails if the requested `dataVersion` already exists in `club_dna_scores`
* overwriting an existing `dataVersion` is not allowed
* partially updating an existing `dataVersion` is not allowed
* adding new rows to an existing `dataVersion` is not allowed
* reusing part of an existing `dataVersion` for new data is not allowed
* applying new source data requires a new `dataVersion`

`dataVersion` is an immutable Club DNA Dataset Snapshot identifier.

Meaning:

* it is not just a label or display version string
* it identifies one specific club DNA dataset at one point in time
* the same `dataVersion` must always mean the same data set
* recommendation results must remain reproducible from the `dataVersion` used when they were generated
* the system must prevent a situation where the same `dataVersion` refers to different club DNA scores over time

There is no persistent "latest version" pointer in the current schema.

The latest usable version is an operational choice made by clients or a future recommendation generation workflow.

If the product needs a persisted latest pointer, sync run history, or active dataset registry, that requires a separate schema Decision and Plan.

Reason:

* recommendation records store or will use `club_data_version` to preserve the club DNA dataset used for scoring
* changing rows behind an existing `dataVersion` would make past recommendation scores and explanations non-reproducible
* exact-equality version comparison only works if each version is immutable
* an immutable snapshot model makes failed, partial, or repeated synchronization attempts observable and rejectable

### 6. Normalization Policy

Synchronization must normalize source data before persistence.

Required normalization:

* `clubs.club_id` -> `clubs.code`
* `clubs.club_name` -> `clubs.name`
* `club-status.club_id` -> `clubs.code`
* `club-status.competition_tier` -> `clubs.competition_tier`
* `club-status.trend` -> `clubs.trend_direction`
* `club-tags.club_id` -> `clubs.code` -> `club_tags.club_id`
* `club-tags.tag` -> `club_tags.tag_name`, removing a leading `#`
* `emotional-dna` metric columns -> `club_dna_scores` rows
* `playstyle-dna` metric columns -> `club_dna_scores` rows
* metric column names -> `dna_definitions.dna_key`

Status values must normalize into the schema namespaces:

* `DYNASTY`
* `TITLE_CONTENDER`
* `CHALLENGER`
* `MID_TABLE`
* `SURVIVAL`
* `RISING`
* `STABLE`
* `REBUILDING`
* `DECLINING`

Unknown status values fail validation.

### 7. Source Gap Fallback Policy

The current source does not provide every required database field.

Use the following MVP fallback policy until the source is expanded:

* `clubs.short_name`: use normalized `clubs.code`
* `clubs.beginner_accessibility`: use `0.00`
* `club_tags.tag_type`: persist `null`
* `club_dna_scores.is_core`: use `false`

These fallback values must be visible in synchronization code and tests.

They must not be silently treated as business-derived values.

Changing these fallbacks or deriving them from a different source requires a separate Decision or explicit Plan update.

### 8. Validation Policy

Synchronization must fail before database writes when validation fails.

Required validation:

* all required source targets are present
* required source columns are present for each included target
* club keys are unique in the `clubs` source
* club keys referenced by status, tags, emotional DNA, and playstyle DNA exist in the `clubs` source
* repeated club names across sheets are consistent when provided
* emotional DNA and playstyle DNA metric columns resolve to active, non-deleted `dna_definitions`
* DNA scores are valid for the `1.00` to `5.00` club score range
* normalized status values are known
* requested `dataVersion` does not already exist in `club_dna_scores`

Validation failures must return a structured failure result and must not partially update synchronized tables.

### 9. Transaction Boundary

External source loading and parsing occur outside the database transaction.

Database writes occur in one transaction after full validation succeeds.

The transaction includes writes to:

* `clubs`
* `club_tags`
* `club_dna_scores`

If any write fails, the database transaction rolls back.

### 10. Runtime Execution

Provide a manual administrator API for MVP synchronization.

Endpoint:

```text
POST /api/admin/football-dna-data/sync
```

Request body:

```json
{
  "dataVersion": "club-v1"
}
```

Rules:

* the synchronization API must not be exposed as a public unauthenticated endpoint
* no application startup sync
* no scheduler in MVP
* no automatic source discovery
* manual execution only

Startup and scheduled synchronization require separate approval because they change operational risk.

### 11. Admin API Security Policy

The manual synchronization API is an administrative operation.

Rules:

* public unauthenticated access is prohibited
* the endpoint belongs under `/api/admin`
* when application authentication/authorization is implemented, the endpoint must require an administrator role or equivalent privileged authority
* until authentication/authorization is implemented, the endpoint must be protected by an explicit temporary restriction

Acceptable temporary restrictions before authentication exists:

* enable the endpoint only in a local or explicitly configured admin environment profile
* require an environment-configured admin sync token or header
* disable the endpoint by default and enable it only through an explicit environment property for trusted operation

The initial implementation must document and test whichever temporary restriction is selected.

The endpoint must not silently run in all environments without an admin restriction.

### 12. Failure Handling

Synchronization is fail-fast for required target loading and validation.

The result should expose:

* success flag
* requested `dataVersion`
* processed target names
* inserted, updated, soft-deleted, and skipped counts by table
* validation errors when present
* failure message when execution fails

Failure results should be observable through the manual API response.

### 13. Change Detection

MVP synchronization uses deterministic key-based comparison during a sync run.

Rules:

* `clubs` changes are detected by comparing normalized source values to the current row identified by `code`
* `club_tags` current set is replaced for synchronized clubs because the table has no stable source row identifier or uniqueness constraint
* `club_dna_scores` changes are represented by a new `dataVersion` snapshot

No persistent sync history, checksum table, or source fingerprint table is introduced in the initial implementation.

---

## Alternatives Considered

### Option A

Adopt Google Sheets API immediately.

Pros:

* richer source metadata
* fewer limitations than published CSV

Cons:

* introduces credentials and operational setup
* exceeds the existing import architecture stage
* unnecessary for the current configured Published CSV workflow

### Option B

Use Published CSV through the existing import abstraction.

Pros:

* reuses current tested source loading path
* avoids new dependencies and credentials
* keeps future transport replacement possible
* matches `DECISION-0003`

Cons:

* source metadata is limited
* source availability depends on published sheet URLs

### Option C

Use full hard replace for synchronized tables.

Pros:

* simple current-state alignment

Cons:

* conflicts with soft-delete policy
* risks losing historical and referential context
* unsafe for versioned recommendation inputs

### Option D

Use upsert for master data and immutable snapshots for club DNA scores.

Pros:

* preserves historical score versions
* supports recommendation reproducibility
* avoids destructive updates
* aligns with `DECISION-0005` and `DECISION-0006`

Cons:

* requires explicit `dataVersion` management
* does not provide a persistent latest pointer

---

## Consequences

Positive:

* Football DNA Data can become the operational source for club recommendation data
* synchronization behavior is deterministic and testable
* previous club DNA snapshots remain reproducible
* database writes are protected by full validation and transaction rollback
* Published CSV can be replaced later without changing sync policy

Negative:

* MVP synchronization depends on explicit admin-provided `dataVersion`
* no persisted latest-version registry exists
* source gap fallbacks are intentionally basic and must be revisited when the source expands
* `club_tags` replacement creates historical soft-deleted rows because no stable source row key exists
* sync history and audit reporting require future schema work

---

## Related Plans

* PLAN-0017

---

## Change History

### 2026-06-12

* Decision created
* Status set to Proposed

### 2026-06-12

* Added admin synchronization API security policy
* Clarified `club_tags` re-synchronization and soft-deleted tag restoration behavior
* Clarified source-removal scope and protection for out-of-scope data
* Expanded `dataVersion` policy to define immutable Club DNA Dataset Snapshots

### 2026-06-12

* Status changed to Accepted after user approval
