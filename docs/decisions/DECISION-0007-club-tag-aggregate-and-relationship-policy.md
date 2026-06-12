# DECISION-0007: Club Tag Aggregate and Relationship Policy

## Metadata

| Field         | Value                                       |
|---------------|---------------------------------------------|
| Decision ID   | DECISION-0007                               |
| Title         | Club Tag Aggregate and Relationship Policy  |
| Status        | Accepted                                    |
| Superseded By |                                             |
| Created At    | 2026-06-12                                  |
| Updated At    | 2026-06-12                                  |

---

## Context

The Club Domain currently implements the `clubs` table only.

The next planned Club Domain step is to implement `club_tags`, which stores descriptive tags for each club. These tags will support recommendation explanations, Team DNA pages, and future AI explanation generation.

Before implementation, the project needs a clear policy for:

* whether `club_tags` is managed as an independent aggregate or as part of the `Club` aggregate
* whether the JPA mapping should use an object relationship to `ClubJpaEntity` or store the foreign key as a scalar `clubId`
* what query scope the first public API should support
* how tag ordering should be interpreted

These choices affect domain boundaries, persistence mapping, API design, and future synchronization behavior.

---

## Decision

Implement `club_tags` as a club-owned child domain model with repository access through the Club Domain, but do not make `Club` load or own tag collections in memory by default.

### 1. Aggregate Boundary

`club_tags` belongs to the Club Domain and is club-owned data.

For the initial implementation, `ClubTag` will be modeled as a separate domain model that references its parent club by `clubId`.

The `Club` domain model must not contain a mutable collection of tags in the initial implementation.

This keeps the current `Club` persistence baseline stable and avoids introducing a larger aggregate graph before tag mutation or synchronization behavior is defined.

### 2. JPA Relationship Policy

`ClubTagJpaEntity` should store `club_id` as a scalar `Long clubId` field rather than a `@ManyToOne ClubJpaEntity` relationship in the initial implementation.

Reasons:

* current use cases are read-oriented
* the API needs tag lookup by club identifier rather than graph traversal
* scalar FK mapping avoids accidental large object graphs and N+1 behavior
* `DATABASE_SCHEMA.md` defines the FK column and does not require object navigation
* the existing `Club` implementation is repository-oriented and does not expose JPA relationships

This does not prevent a future relationship mapping if a later use case demonstrates clear value.

### 3. Query API Scope

The initial API should support both:

* retrieving tags for a specific club
* retrieving all active, non-deleted club tags

Specific-club lookup is required by recommendation result and Team DNA pages.

All-tag lookup is useful for management, validation, and client-side preloading, but it must remain read-only and should use explicit sorting.

### 4. Tag Sorting

Club tag query results must be sorted deterministically.

Default ordering:

1. `display_order` ascending
2. `id` ascending as a tie-breaker

For all-tag lookup, group ordering should additionally be stable by:

1. `club_id` ascending
2. `display_order` ascending
3. `id` ascending

### 5. Active And Deleted Filtering

Normal tag queries must include only:

* `is_active = true`
* `is_deleted = false`

This follows the distinction between business activation state and logical deletion defined by `DECISION-0005`.

### 6. Tag Type

`tag_type` is nullable in `DATABASE_SCHEMA.md`.

The initial implementation may expose `tagType` as nullable and must not invent a classification policy.

Future synchronization or classification work must define how missing `tag_type` values are sourced or derived before assigning values automatically.

---

## Alternatives Considered

### Option A

Model `club_tags` as a collection inside the `Club` aggregate and map JPA with `@OneToMany`.

Pros:

* represents ownership directly in the object graph
* convenient when saving a club and its tags together

Cons:

* expands the existing `Club` aggregate before mutation use cases are defined
* increases risk of accidental eager graph usage or N+1 queries
* complicates the current `clubs`-only persistence baseline
* provides little value for the initial read-only API scope

### Option B

Model `ClubTag` as a separate club-owned domain model using scalar `clubId`.

Pros:

* keeps the first implementation small and deterministic
* aligns with current repository and adapter style
* avoids unnecessary JPA graph complexity
* supports direct read queries efficiently
* remains compatible with future synchronization flows

Cons:

* does not express parent-child ownership through JPA object navigation
* future write workflows may need explicit club existence validation

### Option C

Treat `club_tags` as an independent aggregate unrelated to `Club` except through API composition.

Pros:

* maximizes independence of tag persistence
* simple repository ownership

Cons:

* weakens the schema-defined FK relationship to `clubs`
* does not reflect that tags are meaningful only in the context of a club
* can lead to unclear lifecycle rules

---

## Consequences

Positive:

* `club_tags` can be implemented without destabilizing the existing `Club` aggregate
* read APIs can be deterministic and simple
* normal queries follow soft delete and active-state rules
* future synchronization can write tags by resolved `clubId` without JPA graph coupling

Negative:

* parent club validation must be handled explicitly in application services when write use cases are introduced
* object navigation from `ClubJpaEntity` to tags is not available in the initial implementation
* a future relationship mapping may require a new Plan if stronger aggregate behavior becomes necessary

---

## Related Plans

* PLAN-0013

---

## Change History

### 2026-06-12

* Decision created
* Status set to Proposed

### 2026-06-12

* Decision approved by user
* Status set to Accepted
