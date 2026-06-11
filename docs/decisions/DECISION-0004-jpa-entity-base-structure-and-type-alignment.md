# DECISION-0004: JPA Entity Base Structure and Type Alignment

## Metadata

| Field | Value |
| --- | --- |
| Decision ID | DECISION-0004 |
| Title | JPA Entity Base Structure and Type Alignment |
| Status | Accepted |
| Created At | 2026-06-10 |
| Updated At | 2026-06-10 |

---

## Context

The latest `DATABASE_SCHEMA.md` defines common audit columns for all tables and clarifies schema-level types for fields such as version values and score values.

The current project state shows that:

- some existing assessment JPA entities define audit fields directly
- some existing entity fields do not align with the updated schema types
- the project does not yet have a shared base entity for common audit columns

Before restructuring entities, the project needs a clear decision for:

- whether all JPA entities should inherit a shared `BaseEntity`
- how `createdAt` and `updatedAt` should be implemented
- whether JPA auditing should be enabled globally
- how schema-defined `INT` and `DECIMAL(5,2)` fields should be represented in JPA entities
- whether the alignment rules apply only to current assessment entities or to future entities as well

These choices affect entity consistency, persistence mappings, future entity implementation, and the ability to keep code aligned with the documented schema.

---

## Decision

### 1. Shared Base Entity

All JPA entity classes in the project should inherit a shared `global.infrastructure.entity.BaseEntity`.

This base entity will provide the common audit fields required by `DATABASE_SCHEMA.md`:

- `createdAt`
- `updatedAt`

This rule applies to:

- existing JPA entity classes
- future JPA entity classes added to the project

The intent is to establish one standard persistence baseline rather than treating audit-field inheritance as a per-domain choice.

### 2. Audit Field Implementation

`createdAt` and `updatedAt` should be implemented once in `BaseEntity` as `LocalDateTime` fields and removed from individual entity classes.

Individual JPA entity classes must not duplicate these common audit fields unless a future exceptional case is explicitly justified by a separate Decision.

The shared base entity becomes the only standard ownership point for common audit columns.

### 3. JPA Auditing

JPA auditing should be enabled globally for the project.

The shared `BaseEntity` should use JPA auditing annotations on `LocalDateTime` fields so that:

- `createdAt` is populated automatically on insert
- `updatedAt` is updated automatically on insert and update

This keeps audit-field behavior consistent across entities and avoids repeating timestamp-management logic in individual entity classes.

### 4. Schema Type Alignment for INT Fields

Schema-defined `INT` fields should be represented as `Integer` in JPA entities unless a stronger reason exists to use a different type.

This applies to fields such as:

- version values
- display order values
- explanation or prompt version values
- similar schema-defined integer columns

For the existing assessment entity set, this means version fields that are currently modeled as `String` should be aligned to `Integer` where the schema defines them as `INT`.

### 5. Schema Type Alignment for DECIMAL(5,2) Fields

Schema-defined `DECIMAL(5,2)` fields should be represented as `BigDecimal` in JPA entities.

This applies to score-related values and similar decimal columns where the schema explicitly defines fixed-point precision.

For the existing assessment entity set, this means score fields currently modeled as `int` should be aligned to `BigDecimal` where the schema defines them as `DECIMAL(5,2)`.

### 6. Existing Assessment Entity Alignment

Existing assessment JPA entities should be aligned with `DATABASE_SCHEMA.md` as part of future implementation work that follows this Decision.

That alignment includes:

- moving audit fields into `BaseEntity`
- changing schema-defined `INT` fields to `Integer`
- changing schema-defined `DECIMAL(5,2)` fields to `BigDecimal`
- adding schema-defined fields that are currently missing from existing assessment entities

This Decision defines the structure and type-alignment rules only.

It does not itself authorize unrelated business behavior changes, recommendation logic, or import logic changes.

### 7. Scope of Applicability

This Decision applies to all current and future JPA entity classes in the project, not only the currently implemented assessment entities.

This scope is limited to JPA persistence entity structure and field typing.

It does not apply directly to:

- domain model classes
- application DTOs
- controller request or response models
- non-JPA persistence helper classes

Existing assessment entities are the immediate alignment target because they are the only currently implemented JPA entity set, but the standard is project-wide.

---

## Alternatives Considered

### Option A

Allow each entity to define its own audit fields and choose field types case by case.

Pros:

* minimizes immediate refactoring pressure
* allows local flexibility per entity

Cons:

* creates inconsistent audit-field ownership
* increases duplication across entities
* weakens alignment with the documented schema
* makes future entity implementation less predictable

### Option B

Introduce a shared `BaseEntity`, enable JPA auditing globally, and align entity field types to the documented schema.

Pros:

* creates one consistent standard for audit fields
* improves schema-to-entity alignment
* reduces duplicated persistence structure
* provides a reusable pattern for future entities

Cons:

* requires coordinated changes across existing entities
* may require type changes in existing domain and mapping code

### Option C

Adopt `BaseEntity` only for new entities and leave existing assessment entities unchanged.

Pros:

* reduces short-term refactoring effort
* avoids immediate updates to existing mappings

Cons:

* leaves the project in a mixed structural state
* preserves inconsistency in the current entity set
* delays schema alignment work instead of resolving it

---

## Consequences

Positive:

* all entities share one standard audit-field structure
* entity code becomes more consistent with `DATABASE_SCHEMA.md`
* version and score fields use schema-aligned Java types
* future entity implementation has a clear project-wide baseline

Negative:

* existing entity mappings and related domain translation code may need coordinated updates
* introducing `BigDecimal` for decimal schema fields can require broader type alignment in existing model and test code
* entity-alignment work will require follow-up implementation under an approved Plan

---

## Related Plans

* PLAN-0006

---

## Change History

### 2026-06-10

* Decision created
* Status set to Proposed

### 2026-06-10

* Decision approved by user
* Status set to Accepted
