# DECISION-0010: DNA Definition Domain Ownership Policy

## Metadata

| Field         | Value                                        |
|---------------|----------------------------------------------|
| Decision ID   | DECISION-0010                                |
| Title         | DNA Definition Domain Ownership Policy       |
| Status        | Accepted                                     |
| Superseded By |                                              |
| Created At    | 2026-06-12                                   |
| Updated At    | 2026-06-12                                   |

---

## Context

`dna_definitions` is the master table for Football DNA attributes.

The documented architecture already defines:

* `dna_definitions` belongs to the `dna` domain
* assessment stores user DNA scores by `dna_definition_id`
* club stores club DNA scores by `dna_definition_id`
* recommendation will compare user DNA and club DNA using the same DNA definition namespace

However, the current implementation places `DnaDefinition` domain model, repository interface, JPA entity, Spring Data repository, and persistence adapter under the `assessment` package.

This creates an ownership mismatch:

* `assessment` appears to own shared DNA master data
* `club_dna_scores` avoids depending on `assessment` by using a local native projection over `dna_definitions`
* future `recommendation` code would need either a misplaced `assessment` dependency or another duplicate lookup path

Before implementing recommendation behavior, ownership and dependency direction for DNA master data must be clarified.

---

## Decision

Move `dna_definitions` implementation ownership to an independent `dna` domain.

### 1. Domain Ownership

`dna_definitions` is owned by the `dna` domain.

The following implementation classes should be located under `com.kopite.fd.dna`:

* DNA definition domain model
* DNA definition repository interface
* DNA definition JPA entity
* DNA definition Spring Data JPA repository
* DNA definition persistence adapter

The `assessment` domain must not own `DnaDefinition` classes after the migration.

### 2. Initial Domain Scope

The `dna` domain starts as a read-only master data domain.

Initial responsibilities:

* represent DNA definitions
* read active, non-deleted DNA definitions
* resolve DNA definitions by ID or by category/key when a use case requires it
* preserve `dna_category`, `dna_key`, `display_name`, `description`, `display_order`, and active/deleted filtering semantics

Initial responsibilities do not include:

* public DNA definition API
* admin mutation API
* Google Sheet synchronization for `dna_definitions`
* DNA rubric persistence
* recommendation scoring

### 3. Cross-Domain Reference Policy

Assessment, club, and recommendation domain models should reference DNA definitions by scalar identifier:

* `dnaDefinitionId`

They should not introduce JPA relationships to `DnaDefinitionJpaEntity` in the initial implementation.

Reasons:

* existing schema stores scalar FK columns
* assessment and club scores are independent rows that should not require loading DNA master data
* recommendation needs deterministic score comparisons by ID
* scalar references avoid accidental object graph loading and N+1 behavior

### 4. Cross-Domain Lookup Policy

When another domain needs DNA metadata or validation, it may depend on the `dna` domain read abstraction.

Allowed dependencies:

* application or infrastructure code in another domain may call a `dna` read abstraction when a use case requires DNA metadata
* other domains may use `dna.domain.model` result objects for read-only metadata
* other domains must not depend on `dna.infrastructure` classes
* other domains must not duplicate `DnaDefinitionJpaEntity`

Domain models in assessment, club, and recommendation should continue to store scalar IDs and avoid direct object references.

### 5. API Policy

No public DNA definition API is introduced by the initial migration.

Rationale:

* the current user-facing APIs do not require standalone DNA definition browsing
* assessment questions already expose the required `primaryDnaDefinitionId`
* club DNA score APIs already expose DNA metadata as part of club-specific read models
* adding public API surface should be driven by a concrete frontend or admin requirement

A public or admin DNA API requires a separate approved Plan.

### 6. Enum Policy

`dna_category` remains stored as database master data string values.

Current valid schema values:

* `EMOTIONAL`
* `PLAYSTYLE`

The initial migration should not require a persisted enum.

Domain code may define a non-persistence enum only when it improves validation or ordering, but it must not introduce values outside the documented schema.

### 7. Existing Assessment Behavior

Assessment remains owner of:

* `questions`
* `question_options`
* `option_score_mappings`
* `assessment_dna_scores`

These tables may continue storing `dna_definition_id` scalar references.

Any assessment code that needs DNA definition metadata should use the `dna` domain read abstraction rather than an assessment-owned repository.

### 8. Existing Club DNA Score Behavior

`club_dna_scores` remains owned by the `club` domain.

The existing native projection over `dna_definitions` was acceptable as a temporary bridge under `DECISION-0008`.

After the `dna` domain is introduced, club read behavior should remain correct and should avoid depending on `assessment` DNA classes.

The migration may either:

* keep the current persistence-local native projection if it remains the smallest safe query model, or
* replace metadata lookup with a `dna` read abstraction if doing so does not introduce inefficient N+1 lookup behavior

The migration must not make club code depend on `assessment` DNA classes.

### 9. Database Schema Policy

This Decision does not change the `dna_definitions` table schema.

No table, column, constraint, or FK change is required for the initial migration.

---

## Alternatives Considered

### Option A

Leave DNA definition implementation under `assessment`.

Pros:

* no migration work
* current assessment tests remain untouched

Cons:

* conflicts with `PACKAGE_STRUCTURE.md` and `DATABASE_SCHEMA.md`
* forces future recommendation or club code toward an assessment dependency
* keeps shared master data under a feature-specific domain
* contradicts the migration expectation already recorded in `DECISION-0008`

### Option B

Move `dna_definitions` to an independent read-only `dna` domain.

Pros:

* aligns implementation with documented table ownership
* creates a stable shared master-data boundary
* prevents assessment from owning recommendation-wide DNA vocabulary
* supports future recommendation implementation without misplaced dependencies
* keeps migration scope small because schema and public API do not change

Cons:

* requires package moves and import/test updates
* cross-domain read policy must be explicit to avoid infrastructure coupling

### Option C

Introduce a full public/admin DNA API and write management workflow now.

Pros:

* provides a complete DNA management surface
* may help future admin tools

Cons:

* exceeds current recommendation-preparation requirement
* requires additional API contract decisions
* risks premature design before admin needs and synchronization ownership are settled

---

## Consequences

Positive:

* implementation ownership matches architecture documents
* assessment, club, and future recommendation can share one DNA master-data domain
* future recommendation code can depend on `dna` rather than `assessment`
* no database schema migration is required
* public API surface remains unchanged

Negative:

* existing tests and imports must be updated
* current `FdApplicationTests` mock wiring must move from assessment DNA repository to dna repository
* club DNA score native projection may remain as a pragmatic read model until a dedicated composite query design is needed
* future DNA write/admin behavior still requires a separate Plan and possibly another Decision

---

## Related Plans

* PLAN-0016

---

## Change History

### 2026-06-12

* Decision created
* Status set to Proposed

### 2026-06-12

* Decision approved by user
* Status set to Accepted
