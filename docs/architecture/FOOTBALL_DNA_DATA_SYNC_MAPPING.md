# Football DNA Data Synchronization Mapping

## Purpose

This document defines how Football DNA Data maps to the normalized database schema before any synchronization implementation begins.

It is the design baseline for future synchronization implementation.

It does not define runtime synchronization code, transaction behavior, or final replace/upsert/delete mechanics.

---

## Scope

This document covers the currently imported Football DNA Data targets:

* `clubs`
* `emotional-dna`
* `playstyle-dna`
* `club-status`
* `club-tags`
* `dna-rubric`

It evaluates these targets against the normalized schema in [DATABASE_SCHEMA.md](/C:/Users/nellu/temp/fd_b/docs/architecture/DATABASE_SCHEMA.md).

---

## Source Sheets

| Source Sheet | Imported Target Key | Current Role |
| --- | --- | --- |
| Clubs | `clubs` | Base club master data |
| Emotional DNA | `emotional-dna` | Emotional DNA scores per club |
| Playstyle DNA | `playstyle-dna` | Playstyle DNA scores per club |
| Club Status | `club-status` | Competitive tier and trend metadata per club |
| Club Tags | `club-tags` | Club explanation tags |
| DNA Rubric | `dna-rubric` | Reference rubric for score interpretation |

---

## Target Tables

### Included In Initial Synchronization Scope

* `clubs`
* `club_dna_scores`
* `club_tags`

### Reviewed But Excluded From Initial Synchronization Scope

* `dna_definitions`

### Explicitly Out of Scope

These tables do not receive Football DNA Data directly:

* `users`
* `user_assessments`
* `questions`
* `question_options`
* `option_score_mappings`
* `assessment_answers`
* `assessment_dna_scores`
* `assessment_recommendations`
* `assessment_ai_adjustments`

---

## Mapping Summary

| Source Sheet | Target Table(s) | Status | Notes |
| --- | --- | --- | --- |
| `clubs` | `clubs` | Included | Provides the base club master record keyed by `code` |
| `emotional-dna` | `club_dna_scores` | Included | Requires wide-to-row normalization and DNA key resolution |
| `playstyle-dna` | `club_dna_scores` | Included | Requires wide-to-row normalization and DNA key resolution |
| `club-status` | `clubs` | Included | Overlays status fields onto the base `clubs` record |
| `club-tags` | `club_tags` | Included | Requires club key resolution and tag normalization |
| `dna-rubric` | None in initial sync | Excluded | Does not fit the current normalized schema without additional design |

---

## Detailed Sheet-to-Table Mapping

### 1. `clubs` -> `clubs`

This sheet provides the base row for each club.

#### Column Mapping

| Sheet Column | Database Column | Mapping Rule | Notes |
| --- | --- | --- | --- |
| `club_id` | `clubs.code` | Direct | Stable business key for cross-sheet joins |
| `club_name` | `clubs.name` | Direct | Official display name |
| `league` | `clubs.league` | Direct | Current source uses `EPL` values |
| `country` | `clubs.country` | Direct | Direct text mapping |
| `is_active` | `clubs.is_active` | Direct boolean normalization | `TRUE` / `FALSE` source values |
| `season` | None | Excluded | No current destination column |
| `city` | None | Excluded | No current destination column |
| `note` | None | Excluded | Operational note only |

#### Required Database Fields Without Source Columns

| Database Column | Status | Required Action |
| --- | --- | --- |
| `clubs.short_name` | Missing | Add source column or define a deterministic derivation rule in a separate approved design |
| `clubs.competition_tier` | Filled by `club-status` | Must be populated after base `clubs` sync |
| `clubs.trend_direction` | Filled by `club-status` | Must be populated after base `clubs` sync |
| `clubs.beginner_accessibility` | Missing | Add source column or define a derived scoring policy in a separate approved design |
| `clubs.logo_url` | Missing | Add source column if needed |
| `clubs.primary_color` | Missing | Add source column if needed |
| `clubs.secondary_color` | Missing | Add source column if needed |

#### Application-Managed Fields

These fields are not mapped from the sheet:

* `clubs.id`
* `clubs.created_at`
* `clubs.updated_at`
* `clubs.is_deleted`

---

### 2. `club-status` -> `clubs`

This sheet supplements the base `clubs` row.

It must not create independent club rows.

It should update the matching `clubs` record resolved by:

* `club-status.club_id`
* `clubs.code`

#### Column Mapping

| Sheet Column | Database Column | Mapping Rule | Notes |
| --- | --- | --- | --- |
| `club_id` | `clubs.code` | Join key only | Used to locate the base club row |
| `competition_tier` | `clubs.competition_tier` | Value normalization required | Source values appear human-readable and localized |
| `trend` | `clubs.trend_direction` | Rename and normalize | Source column name differs from schema |
| `club_name` | None | Validation only | Should match the `clubs` sheet name |
| `season` | None | Excluded | No current destination column |
| `source_type` | None | Excluded | Source provenance only |
| `note` | None | Excluded | Operational note only |

#### Required Normalization

The source values in this sheet are not ready for direct persistence.

Required normalization work:

* map source `competition_tier` labels into the schema namespace used by `clubs.competition_tier`
* map source `trend` labels into the schema namespace used by `clubs.trend_direction`

This mapping table must be defined explicitly before synchronization implementation.

---

### 3. `club-tags` -> `club_tags`

This sheet produces one `club_tags` row per tag entry.

#### Column Mapping

| Sheet Column | Database Column | Mapping Rule | Notes |
| --- | --- | --- | --- |
| `club_id` | `club_tags.club_id` | Resolve through `clubs.code` -> `clubs.id` | Foreign key resolution required |
| `tag` | `club_tags.tag_name` | Normalize text | Remove leading `#` before persistence because `#` is presentation-only |
| `display_order` | `club_tags.display_order` | Direct integer normalization | Preserves UI ordering |
| `is_active` | `club_tags.is_active` | Direct boolean normalization | `TRUE` / `FALSE` source values |
| `club_name` | None | Validation only | Should match the `clubs` sheet name |
| `note` | None | Excluded | Operational note only |

#### Required Database Fields Without Source Columns

| Database Column | Status | Required Action |
| --- | --- | --- |
| `club_tags.tag_type` | Missing | Add source column or define a post-import classification rule in a separate approved design |

#### Application-Managed Fields

These fields are not mapped from the sheet:

* `club_tags.id`
* `club_tags.created_at`
* `club_tags.updated_at`
* `club_tags.is_deleted`

---

### 4. `emotional-dna` -> `club_dna_scores`

This sheet contains wide-format emotional DNA metrics.

It does not map one row to one database row.

Each source row must be expanded into multiple `club_dna_scores` rows.

#### Metric Columns

These source columns map to `dna_definitions.dna_key` values and generate `club_dna_scores` rows:

* `club_prestige`
* `fan_culture`
* `underdog`
* `growth`
* `star_power`
* `drama`
* `local_identity`
* `popularity`

#### Row Generation Rule

For each source row:

1. resolve the club by `club_id` -> `clubs.code`
2. for each emotional metric column
3. resolve the DNA definition by:
   * `dna_category = EMOTIONAL`
   * `dna_key = <metric column name>`
4. create one `club_dna_scores` row

#### Column Mapping

| Sheet Column | Database Column | Mapping Rule | Notes |
| --- | --- | --- | --- |
| `club_id` | `club_dna_scores.club_id` | Resolve through `clubs.code` -> `clubs.id` | Foreign key resolution required |
| each metric column | `club_dna_scores.score` | Wide-to-row normalization | One score row per metric |
| each metric column name | `club_dna_scores.dna_definition_id` | Resolve through `dna_definitions` | Requires matching `dna_key` and `dna_category` |
| none | `club_dna_scores.data_version` | Application-assigned | Must use the synchronization snapshot identifier such as `club-v1` |
| `club_name` | None | Validation only | Should match the `clubs` sheet name |
| `season` | None | Excluded | No current destination column |
| `source_type` | None | Excluded | Source provenance only |
| `note` | None | Excluded | Operational note only |

#### Required Database Fields Without Source Columns

| Database Column | Status | Required Action |
| --- | --- | --- |
| `club_dna_scores.is_core` | Missing | Add source column or define a separate rule for core DNA identification |
| `club_dna_scores.data_version` | Missing from sheet by design | Must be assigned by synchronization logic according to `DECISION-0006` |

#### Application-Managed Fields

These fields are not mapped from the sheet:

* `club_dna_scores.id`
* `club_dna_scores.created_at`
* `club_dna_scores.updated_at`
* `club_dna_scores.is_deleted`

---

### 5. `playstyle-dna` -> `club_dna_scores`

This sheet follows the same normalization pattern as `emotional-dna`.

#### Metric Columns

These source columns map to `dna_definitions.dna_key` values and generate `club_dna_scores` rows:

* `possession`
* `directness`
* `pressing`
* `organization`
* `creativity`
* `transition_speed`

#### Row Generation Rule

For each source row:

1. resolve the club by `club_id` -> `clubs.code`
2. for each playstyle metric column
3. resolve the DNA definition by:
   * `dna_category = PLAYSTYLE`
   * `dna_key = <metric column name>`
4. create one `club_dna_scores` row

#### Column Mapping

| Sheet Column | Database Column | Mapping Rule | Notes |
| --- | --- | --- | --- |
| `club_id` | `club_dna_scores.club_id` | Resolve through `clubs.code` -> `clubs.id` | Foreign key resolution required |
| each metric column | `club_dna_scores.score` | Wide-to-row normalization | One score row per metric |
| each metric column name | `club_dna_scores.dna_definition_id` | Resolve through `dna_definitions` | Requires matching `dna_key` and `dna_category` |
| none | `club_dna_scores.data_version` | Application-assigned | Must use the synchronization snapshot identifier such as `club-v1` |
| `club_name` | None | Validation only | Should match the `clubs` sheet name |
| `season` | None | Excluded | No current destination column |
| `source_type` | None | Excluded | Source provenance only |
| `raw_source` | None | Excluded | Source provenance only |
| `note` | None | Excluded | Operational note only |

#### Required Database Fields Without Source Columns

| Database Column | Status | Required Action |
| --- | --- | --- |
| `club_dna_scores.is_core` | Missing | Add source column or define a separate rule for core DNA identification |
| `club_dna_scores.data_version` | Missing from sheet by design | Must be assigned by synchronization logic according to `DECISION-0006` |

---

### 6. `dna-rubric` -> no initial target table

This sheet was reviewed and is synchronization-relevant as reference data, but it does not fit the current normalized schema as a direct persistence target.

#### Why It Does Not Fit Directly

The sheet structure is rubric-oriented:

* `category`
* `metric_key`
* `metric_name`
* `score`
* `anchor_club`
* `description`

The current schema does not have a table for rubric anchors or score-level descriptive references.

The closest current table is `dna_definitions`, but the mapping is incomplete:

| Sheet Column | Possible Database Column | Problem |
| --- | --- | --- |
| `category` | `dna_definitions.dna_category` | Requires localized value normalization |
| `metric_key` | `dna_definitions.dna_key` | Possible direct mapping |
| `metric_name` | `dna_definitions.display_name` | Possible direct mapping |
| `description` | `dna_definitions.description` | Ambiguous because the rubric contains score-level descriptions, not one stable metric-level description |
| `score` | None | No destination column |
| `anchor_club` | None | No destination column |

#### Initial Synchronization Decision

`dna-rubric` is excluded from initial normalized database synchronization.

It should be treated as:

* a reference sheet for manual review
* a validation aid for DNA namespace alignment
* a candidate for future schema expansion or a separate rubric table

---

## Target Table Coverage

| Target Table | Source Sheet(s) | Initial Sync Status | Notes |
| --- | --- | --- | --- |
| `clubs` | `clubs`, `club-status` | Included | Base + overlay model |
| `club_dna_scores` | `emotional-dna`, `playstyle-dna` | Included | Requires unpivot and FK resolution |
| `club_tags` | `club-tags` | Included | Requires tag normalization |
| `dna_definitions` | `dna-rubric` | Excluded | Current source/schema mismatch |

---

## Missing Fields

### Missing From Football DNA Data But Required By Included Target Tables

| Target Table | Database Column | Gap |
| --- | --- | --- |
| `clubs` | `short_name` | No source column |
| `clubs` | `beginner_accessibility` | No source column |
| `club_tags` | `tag_type` | No source column |
| `club_dna_scores` | `is_core` | No source column |

### Missing From Football DNA Data But Application-Assigned By Design

| Target Table | Database Column | Reason |
| --- | --- | --- |
| `club_dna_scores` | `data_version` | Assigned by the synchronization run under `DECISION-0006` |

### Missing From Football DNA Data But Optional In Current Schema

| Target Table | Database Column | Gap |
| --- | --- | --- |
| `clubs` | `logo_url` | No source column |
| `clubs` | `primary_color` | No source column |
| `clubs` | `secondary_color` | No source column |

---

## Extra Fields In Source Sheets

These fields currently have no destination column in the normalized schema:

| Source Sheet | Extra Fields |
| --- | --- |
| `clubs` | `season`, `city`, `note` |
| `club-status` | `season`, `source_type`, `note` |
| `club-tags` | `note` |
| `emotional-dna` | `season`, `source_type`, `note`, repeated `club_name` |
| `playstyle-dna` | `season`, `source_type`, `raw_source`, `note`, repeated `club_name` |
| `dna-rubric` | `score`, `anchor_club`, score-level `description` |

Repeated `club_name` values outside the `clubs` sheet should be treated as cross-sheet validation fields, not persistence fields.

---

## Inconsistent Fields

| Issue | Description | Impact |
| --- | --- | --- |
| `club_id` vs `code` | Source uses `club_id`; schema uses `code` | Requires explicit join-key mapping |
| `trend` vs `trend_direction` | Source column name differs from schema column name | Requires rename and value normalization |
| `tag` presentation format | Source values include leading `#`; schema note says `#` is presentation-only | Requires tag text normalization |
| localized status values | `competition_tier` and `trend` source values appear localized/human-readable | Cannot persist directly into schema enum namespace |
| localized rubric category values | `dna-rubric.category` appears localized | Requires mapping to `EMOTIONAL` / `PLAYSTYLE` if used later |
| source text encoding | Existing artifact output shows mojibake for non-English text | Source retrieval or artifact encoding must be verified before synchronization implementation |

---

## Normalization Issues

### 1. Wide-to-Row Transformation Required

`emotional-dna` and `playstyle-dna` are wide sheets.

`club_dna_scores` is a row-based normalized table.

Synchronization must unpivot metric columns into one row per:

* club
* DNA definition
* data version

### 2. Multi-Sheet Single-Table Assembly

`clubs` is not populated from one sheet alone.

It requires:

* base fields from `clubs`
* status fields from `club-status`

Synchronization must treat these as one assembled club master record.

### 3. Foreign Key Resolution Required

`club_dna_scores` and `club_tags` do not persist source `club_id` directly.

They must resolve:

* source `club_id`
* `clubs.code`
* `clubs.id`

`club_dna_scores` must also resolve DNA keys to `dna_definitions.id`.

### 4. Rubric Data Has No Current Table

`dna-rubric` contains score-anchor reference data.

The current schema does not preserve that structure.

This is a schema-design gap, not just a column-mapping gap.

### 5. Missing Business Fields In Source

Several included target-table fields have no source:

* `clubs.short_name`
* `clubs.beginner_accessibility`
* `club_tags.tag_type`
* `club_dna_scores.is_core`

These cannot be guessed during implementation.

They require either:

* source-sheet expansion
* explicit deterministic derivation rules
* schema revision

---

## Recommended Future Synchronization Order

The recommended logical order for future implementation is:

1. Validate the presence and structural integrity of all required sheets
2. Validate club-key consistency across `clubs`, `club-status`, `club-tags`, `emotional-dna`, and `playstyle-dna`
3. Validate DNA key coverage against existing `dna_definitions`
4. Determine the synchronization snapshot identifier for the run, such as `club-v1`
5. Synchronize base `clubs` rows from the `clubs` sheet
6. Apply `club-status` overlays to the synchronized `clubs` rows
7. Synchronize `club_tags` rows after `clubs` exist
8. Synchronize `club_dna_scores` rows after both `clubs` and `dna_definitions` are validated and available
9. Record excluded or deferred rubric handling for `dna-rubric`

### Dependency Notes

* `club-status` depends on `clubs`
* `club-tags` depends on `clubs`
* `club_dna_scores` depends on both `clubs` and `dna_definitions`
* `dna-rubric` should not block the initial normalized synchronization unless a later Decision makes it a required persistence target

---

## Unresolved Issues And Follow-Up Recommendations

### Unresolved Issues

1. The source does not provide values for `short_name`, `beginner_accessibility`, `tag_type`, or `is_core`.
2. The source artifacts show encoding issues for non-English text.
3. The current schema has no rubric table for `dna-rubric`.
4. The normalization rules for `competition_tier` and `trend_direction` values are not yet defined.

### Follow-Up Recommendations

1. Create a dedicated implementation Plan for normalized Football DNA Data synchronization.
2. Define the normalization dictionary for `competition_tier` and `trend_direction` before coding.
3. Decide whether `dna_definitions` should remain separately managed or become sheet-driven through a revised source/schema design.
4. Decide how `is_core` will be sourced or derived before implementing `club_dna_scores` synchronization.
5. Verify UTF-8 handling of published-sheet content before relying on textual fields for production synchronization.

