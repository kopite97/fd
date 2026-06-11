# PLAN-0011: Football DNA Data Sheet-to-Schema Synchronization Mapping

## Metadata

| Field      | Value                                                             |
| ---------- | ----------------------------------------------------------------- |
| Plan ID    | PLAN-0011                                                         |
| Title      | Football DNA Data Sheet-to-Schema Synchronization Mapping         |
| Type       | Design                                                            |
| Status     | Completed                                                         |
| Created At | 2026-06-11                                                        |
| Updated At | 2026-06-11                                                        |

---

## Goal

Define and document how Football DNA Data sheets map to the normalized database schema before any synchronization implementation begins.

The output of this Plan is a synchronization mapping document that can be used as the implementation baseline for future database synchronization work.

---

## Scope

This Plan includes:

* reviewing Football DNA Data source sheets and currently imported target structures
* reviewing `docs/architecture/DATABASE_SCHEMA.md`
* identifying all source sheets that participate in database synchronization
* identifying all database tables that should receive Football DNA Data
* defining sheet-to-table mappings
* defining column-to-column mappings
* defining explicit inclusion and exclusion decisions for synchronization scope
* identifying source fields that have no destination column
* identifying required database fields that are missing from the source sheets
* identifying normalization issues that should be resolved before synchronization implementation
* defining the recommended future synchronization order
* producing a Markdown synchronization mapping document

---

## Out of Scope

This Plan does not include:

* implementing synchronization code
* creating or modifying runtime synchronization APIs
* PostgreSQL schema migration execution
* club domain code implementation
* recommendation domain code implementation
* recommendation scoring changes
* import transport changes
* automatic or scheduled synchronization
* finalizing replace/upsert/soft-delete runtime behavior

---

## Tasks

### Phase 1

* [x] Review accepted Decisions and completed Plans relevant to Football DNA Data import and schema ownership
* [x] Review `docs/resources/FOOTBALL_DNA_DATA.md`, current import artifacts, and `docs/architecture/DATABASE_SCHEMA.md`
* [x] Inventory all synchronization source sheets and target tables

### Phase 2

* [x] Define sheet-to-table mappings for all synchronization-relevant sheets
* [x] Define column-to-column mappings for each target table
* [x] Identify fields that are missing, redundant, or inconsistent between sheets and schema
* [x] Identify normalization issues that should be resolved before implementation

### Phase 3

* [x] Define the recommended synchronization order for future implementation
* [x] Produce the synchronization mapping document in Markdown
* [x] Record unresolved issues, assumptions rejected by documentation, and implementation follow-ups

---

## Dependencies

Related Plans (Optional):

* PLAN-0003
* PLAN-0004
* PLAN-0005

Related Decisions (Optional):

* DECISION-0003
* DECISION-0006

---

## Progress Log

### 2026-06-11

* Plan created
* Status set to Proposed

### 2026-06-11

* Plan approved by user
* Status set to In Progress

### 2026-06-11

* Reviewed `DECISION-0003`, `DECISION-0006`, `PLAN-0003`, `PLAN-0004`, and `PLAN-0005`
* Reviewed `docs/resources/FOOTBALL_DNA_DATA.md`, import artifacts, and `docs/architecture/DATABASE_SCHEMA.md`
* Identified all currently imported Football DNA Data sheets and all synchronization target tables
* Produced `docs/architecture/FOOTBALL_DNA_DATA_SYNC_MAPPING.md`
* Documented included targets, excluded targets, column mappings, missing fields, extra fields, inconsistent fields, normalization issues, and the recommended synchronization order
* Status set to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: not applicable unless document generation tooling is introduced
* Unit tests: not applicable
* Integration tests: not applicable
* Manual verification:
  * confirm every imported Football DNA Data target is accounted for
  * confirm every target table in scope has an explicit mapping decision or explicit exclusion
  * confirm missing fields, inconsistent fields, and normalization issues are documented
  * confirm the recommended synchronization order is documented
* Validation result:
  * all six currently imported Football DNA Data targets were reviewed
  * included target tables were documented as `clubs`, `club_dna_scores`, and `club_tags`
  * `dna_definitions` was explicitly reviewed and excluded from initial synchronization scope
  * excluded assessment and recommendation tables were explicitly documented
  * the synchronization mapping document was created at `docs/architecture/FOOTBALL_DNA_DATA_SYNC_MAPPING.md`

---

## Completion Criteria

This Plan is considered complete when:

* [x] All tasks are completed
* [x] A synchronization mapping document exists in Markdown
* [x] All currently imported Football DNA Data sheets in scope are mapped or explicitly excluded
* [x] All target tables in scope are mapped or explicitly excluded
* [x] Column-level mappings are documented for each included target table
* [x] Every synchronization-relevant spreadsheet column is mapped, explicitly ignored, or documented as a follow-up issue
* [x] Missing fields, extra fields, inconsistent fields, and normalization issues are documented
* [x] Recommended synchronization order is documented
* [x] Scope requirements are satisfied
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
