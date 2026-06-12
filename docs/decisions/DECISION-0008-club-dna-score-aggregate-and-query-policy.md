# DECISION-0008: Club DNA Score Aggregate and Query Policy

## Metadata

| Field         | Value                                            |
|---------------|--------------------------------------------------|
| Decision ID   | DECISION-0008                                    |
| Title         | Club DNA Score Aggregate and Query Policy        |
| Status        | Accepted                                         |
| Superseded By |                                                  |
| Created At    | 2026-06-12                                       |
| Updated At    | 2026-06-12                                       |

---

## Context

The Club Domain currently implements `clubs` and `club_tags`.

The next planned Club Domain step is to implement `club_dna_scores`, which stores club DNA score snapshots used by future recommendation logic.

`club_dna_scores` references:

* `clubs.id`
* `dna_definitions.id`
* `data_version`, which must align with `DECISION-0006`

The project needs a clear policy before implementation for:

* whether club DNA scores are managed as part of the `Club` aggregate or as separate club-owned snapshot rows
* whether JPA should use object relationships or scalar foreign-key fields
* how Emotional DNA and Playstyle DNA are represented
* whether DNA category should be duplicated in `club_dna_scores`
* what score range is valid
* what query APIs should be introduced before recommendation engine implementation

These choices affect persistence mapping, recommendation reproducibility, API response shape, and future synchronization behavior.

---

## Decision

Implement `club_dna_scores` as club-owned versioned snapshot data under the Club Domain.

### 1. Aggregate Boundary

`club_dna_scores` belongs to the Club Domain and is club-owned recommendation input data.

For the initial implementation, `ClubDnaScore` will be modeled as a separate domain model that references:

* `clubId`
* `dnaDefinitionId`
* `dataVersion`

The `Club` domain model must not contain a mutable collection of DNA scores in the initial implementation.

This preserves the existing `Club` persistence baseline and keeps versioned snapshot rows queryable without loading a larger object graph.

### 2. JPA Relationship Policy

`ClubDnaScoreJpaEntity` should store `club_id` and `dna_definition_id` as scalar fields:

* `Long clubId`
* `Long dnaDefinitionId`

The initial implementation should not use `@ManyToOne ClubJpaEntity` or `@ManyToOne DnaDefinitionJpaEntity`.

Reasons:

* current use cases are read-oriented
* recommendation queries need snapshot rows by `clubId` and `dataVersion`
* scalar FK mapping avoids accidental object graph loading and N+1 behavior
* `DATABASE_SCHEMA.md` defines FK columns but does not require object navigation
* the existing `Club` and `ClubTag` implementations use repository-oriented scalar ID references

This does not prevent a future relationship mapping if a later use case demonstrates clear value.

### 3. Emotional And Playstyle DNA Representation

Emotional DNA and Playstyle DNA should be managed in one normalized `ClubDnaScore` structure.

`club_dna_scores` must not duplicate the DNA category.

DNA type/category is owned by `dna_definitions.dna_category` and should be resolved through `dna_definition_id` when needed.

`dna_definitions` belongs to the `dna` domain according to `PACKAGE_STRUCTURE.md` and `DATABASE_SCHEMA.md`.

The current placement of DNA definition implementation under the `assessment` package is transitional and must not become a new dependency direction for Club Domain work.

The Club Domain must not depend on `assessment` domain models, repositories, services, or infrastructure to resolve DNA metadata.

Valid categories for the current system are:

* `EMOTIONAL`
* `PLAYSTYLE`

The initial read API may expose DNA metadata only through one of these acceptable approaches:

* a `dna` domain read abstraction, if the DNA domain has been implemented
* a persistence-layer read projection over `dna_definitions` that is local to the Club read adapter and does not expose `assessment` package types
* a reduced response that exposes `dnaDefinitionId` only, if DNA metadata ownership has not yet been implemented and no approved Plan includes a transitional projection

Persistence ownership of category remains in `dna_definitions`.

### 4. Enum Policy

The Club Domain may define a domain enum for API filtering or result classification only when the value is sourced from `dna_definitions.dna_category`.

The enum must not introduce categories beyond the documented schema values.

The initial implementation should avoid persisting category as an enum in `club_dna_scores` because there is no `dna_category` column in that table.

### 5. Score Range Policy

Club DNA scores use the initial MVP scale documented in `DATABASE_SCHEMA.md` and project resources:

* minimum: `1.00`
* maximum: `5.00`

The `ClubDnaScore` domain model should validate this range when constructed from application-owned inputs.

Persistence reads may still map existing database values, but tests and write-oriented construction paths must enforce the documented range.

Changing to a `0-100` scale requires a separate Decision because it affects recommendation scoring, source data interpretation, and historical reproducibility.

### 6. Version Policy

`dataVersion` must follow `DECISION-0006`.

Rules:

* stored as `String`
* maps to `club_dna_scores.data_version`
* example value: `club-v1`
* compared by exact equality only
* not sorted or compared numerically
* immutable once persisted

Normal recommendation-oriented lookups must require an explicit `dataVersion`.

### 7. Query API Scope

The initial API should support both:

* retrieving DNA scores for a specific club and data version
* retrieving all club DNA scores for a data version

Specific-club lookup is required by Team DNA pages and recommendation explanation flows.

All-score lookup is useful for recommendation engine input loading and validation, but it must require `dataVersion` to avoid mixing snapshots.

### 8. Query Filtering And Sorting

Normal reads must include only:

* `is_deleted = false`

Unlike `club_tags`, `club_dna_scores` has no `is_active` column.

Default ordering for a specific club:

1. DNA category order: `EMOTIONAL`, then `PLAYSTYLE`
2. `dna_definitions.display_order` ascending
3. `club_dna_scores.id` ascending

Default ordering for all scores:

1. `club_id` ascending
2. DNA category order: `EMOTIONAL`, then `PLAYSTYLE`
3. `dna_definitions.display_order` ascending
4. `club_dna_scores.id` ascending

### 9. Recommendation Algorithm Scope

This Decision enables future recommendation work by making club DNA snapshots queryable.

It does not define or implement:

* similarity calculation
* core DNA bonus calculation
* beginner adjustment
* TOP5 or TOP3 selection
* AI refinement

Those require separate approved Plans and, if algorithm behavior changes, separate Decisions.

### 10. DNA Domain Migration Awareness

This Decision assumes `dna_definitions` will be owned by the `dna` domain.

The `club_dna_scores` implementation must remain compatible with that future migration.

Rules:

* do not create new Club Domain dependencies on the current `assessment` package DNA implementation
* do not duplicate permanent DNA master-data classes under `club`
* if a temporary projection is used to expose DNA metadata, keep it persistence-local and read-only
* a future DNA Domain migration may replace the temporary projection with a `dna` domain repository or query service without changing the `ClubDnaScore` domain model
* moving `dna_definitions` to the `dna` domain requires a separate approved Plan unless it is explicitly added to the current Plan scope

### 11. is_core Policy

`is_core` is persisted and exposed because it exists in `DATABASE_SCHEMA.md`.

This Decision does not define how `is_core` is sourced or derived during synchronization.

Until a synchronization-specific Decision or Plan defines that policy, the read model must treat `isCore` as stored data only.

---

## Alternatives Considered

### Option A

Model club DNA scores as collections inside the `Club` aggregate with JPA relationships to `ClubJpaEntity` and `DnaDefinitionJpaEntity`.

Pros:

* object ownership is explicit
* can be convenient for rich aggregate mutation workflows

Cons:

* expands the `Club` aggregate before write and synchronization behavior is defined
* risks accidental large object graphs and N+1 queries
* makes versioned snapshot loading less explicit
* conflicts with the current scalar-ID style used by `ClubTag`

### Option B

Model `ClubDnaScore` as a separate club-owned snapshot model using scalar FK fields.

Pros:

* keeps implementation small and deterministic
* aligns with existing `ClubTag` persistence style
* supports efficient lookup by `clubId` and `dataVersion`
* preserves versioned snapshot semantics
* avoids duplicating DNA category in `club_dna_scores`

Cons:

* parent club and DNA definition validation must be explicit in future write/synchronization workflows
* object navigation from JPA entities is not available in the initial implementation

### Option C

Duplicate DNA category directly into `club_dna_scores`.

Pros:

* simple filtering without joining `dna_definitions`
* API response can expose category directly from the score row

Cons:

* conflicts with the current normalized schema
* duplicates master data owned by `dna_definitions`
* risks category drift between score rows and DNA definitions
* requires schema changes outside the requested scope

---

## Consequences

Positive:

* club DNA scores become queryable without destabilizing the existing Club aggregate
* score snapshots remain tied to explicit `dataVersion` values
* Emotional and Playstyle DNA remain normalized through `dna_definitions`
* Club Domain work does not deepen the current transitional `assessment` ownership of DNA definitions
* future recommendation engine work can load deterministic club DNA inputs
* implementation remains consistent with `DECISION-0006` and `DECISION-0007`

Negative:

* API queries that expose DNA category/display metadata need a `dna` read abstraction or a temporary read-only projection over `dna_definitions`
* future synchronization workflows must validate club and DNA definition existence explicitly
* changing score scale or algorithm behavior still requires separate approval
* the current misplaced DNA definition implementation may need a separate migration Plan before richer DNA metadata APIs are expanded

---

## Related Plans

* PLAN-0014

---

## Change History

### 2026-06-12

* Decision created
* Status set to Proposed

### 2026-06-12

* Decision approved by user
* Status set to Accepted
