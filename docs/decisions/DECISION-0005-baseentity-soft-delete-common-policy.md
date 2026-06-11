# DECISION-0005: BaseEntity Soft Delete Common Policy

## Metadata

| Field         | Value |
|---------------|-------|
| Decision ID   | DECISION-0005 |
| Title         | BaseEntity Soft Delete Common Policy |
| Status        | Accepted |
| Superseded By |  |
| Created At    | 2026-06-11 |
| Updated At    | 2026-06-11 |

---

## Context

The project currently uses `global.infrastructure.entity.BaseEntity` as the shared JPA base structure for all entities.

`BaseEntity` currently provides common audit fields:

- `created_at`
- `updated_at`

All JPA entities inherit this base structure.

The project now requires a common deletion policy before broader Football DNA Data database synchronization and Club domain implementation proceed.

The current project state does not yet define:

- whether soft delete is a project-wide standard
- whether physical delete is allowed for business entities
- how repository delete methods should behave
- how source disappearance should be reflected during synchronization
- how snapshot/history/versioned records should be preserved while still following a common deletion model

The project has decided to adopt a common soft delete policy by adding `is_deleted` to `BaseEntity` and aligning `DATABASE_SCHEMA.md` and `ENTITY_CONVENTIONS.md` accordingly.

This decision is required because the deletion model affects:

- all current and future JPA entities
- normalized synchronization behavior
- repository deletion rules
- schema conventions
- future recommendation reproducibility and historical data handling

---

## Decision

The project will add `is_deleted` to `BaseEntity` and adopt `is_deleted` as the standard soft delete field for all JPA entities.

### 1. BaseEntity Ownership

`BaseEntity` will own the following common columns for all JPA entities:

- `created_at`
- `updated_at`
- `is_deleted`

`created_at` and `updated_at` remain audit fields.

`is_deleted` becomes the common logical deletion field inherited by all entities.

This establishes one project-wide persistence baseline for audit and deletion lifecycle state.

### 2. Meaning of is_deleted

`is_deleted` indicates whether a persisted record has been logically deleted.

Rules:

- `false` means the record is not logically deleted
- `true` means the record is logically deleted and must be excluded from normal business reads unless a use case explicitly requires deleted records

Default meaning:

- all newly created records start with `is_deleted = false`

`is_deleted` represents persistence lifecycle deletion state.

It must not be used as a replacement for business availability or exposure semantics.

### 3. Meaning of is_active

`is_active` and `is_deleted` have different responsibilities.

`is_active` means:

- whether the record is currently active, selectable, exposed, or valid for business use

`is_deleted` means:

- whether the record has been logically removed from normal application use

Implications:

- a record may be inactive without being deleted
- a deleted record should not be treated as a normal active business record
- `is_active` is a business-state field
- `is_deleted` is a persistence deletion-state field

### 4. Repository Delete Policy

For business entities, physical delete is not the default operation.

Rules:

- application workflows must prefer logical delete using `is_deleted`
- repository-level physical delete must not be the standard behavior for business data
- delete semantics should be implemented as explicit state change behavior where appropriate
- normal query paths should exclude logically deleted records

Physical delete is allowed only when one of the following is true:

- the record is technical or transient and not part of business history
- the record was created in a failed or rollback-safe workflow and has not become a committed business record
- cleanup is required for non-business test or temporary data
- a later accepted Decision explicitly allows physical deletion for a specific table or workflow

### 5. Snapshot / History / Versioned Data Policy

Snapshot/history/versioned records must still inherit `is_deleted` through `BaseEntity`, but soft delete is not the default operational mechanism for those tables.

Rules:

- snapshot/history/versioned data should normally be preserved as committed records
- synchronization and recommendation workflows must not use logical deletion as a substitute for version preservation
- versioned records should remain queryable by their committed version boundaries
- `is_deleted` may exist structurally on those tables because it is inherited from `BaseEntity`
- however, business workflows should not routinely mark committed snapshot/versioned records as deleted unless a future explicit Decision requires it

This preserves recommendation reproducibility and historical traceability.

### 6. Synchronization Deletion Policy

For Football DNA Data synchronization:

- source disappearance must not default to physical delete
- synchronization workflows should prefer logical delete or business-state updates depending on table semantics
- if the table already uses `is_active` to represent current availability, synchronization may set `is_active=false` without setting `is_deleted=true` when the data is merely no longer active in the latest source state
- `is_deleted=true` should be used when the business intends the record to be logically removed from normal persistence use rather than merely inactive

This distinction is required so that current-state absence and logical deletion are not conflated.

### 7. New Entity Policy

All new JPA entities must inherit `is_deleted` through `BaseEntity`.

Rules for new entities:

- no entity may define a separate deletion field name instead of `is_deleted`
- new business entities must follow the common soft delete policy by default
- any exception that wants to rely primarily on physical delete must be justified by a later explicit Decision or Plan-scoped approved rule if the impact is minor and local

### 8. Query Policy

Normal business queries should exclude logically deleted rows.

Implications:

- repository methods and adapters should treat `is_deleted=false` as the default read scope
- use cases that require deleted rows must opt in explicitly
- synchronization, audit, validation, or administrative recovery workflows may read deleted rows when required

The exact repository filtering implementation may vary by table and adapter design, but the semantic rule is project-wide.

### 9. DATABASE_SCHEMA.md Alignment

`DATABASE_SCHEMA.md` must be updated so that Common Audit Columns include:

- `created_at`
- `updated_at`
- `is_deleted`

This means every documented table inherits `is_deleted` through the shared base entity model.

Table-level sections do not need to redundantly redefine `is_deleted` if the document clearly states that all tables inherit the common columns.

### 10. ENTITY_CONVENTIONS.md Alignment

`ENTITY_CONVENTIONS.md` must be updated to reflect that `BaseEntity` owns:

- `createdAt`
- `updatedAt`
- `isDeleted`

It should also define:

- `isDeleted` as the standard logical deletion field
- default non-deleted creation state
- the distinction between `isActive` and `isDeleted`
- the rule that physical delete is exceptional rather than default
- the rule that snapshot/history/versioned entities inherit the field structurally but must preserve historical records operationally

### 11. Recommendation Domain Impact

This decision must not weaken recommendation reproducibility.

Implications:

- future recommendation logic must still rely on preserved versioned snapshot inputs
- versioned tables such as `club_dna_scores` must remain preserved rather than being routinely deleted
- `is_deleted` does not replace version boundaries
- recommendation flows must continue to treat historical snapshots as durable records
- deletion semantics must not be used to erase committed historical recommendation inputs

### 12. created_at / updated_at Preservation

This decision does not change the existing behavior of:

- `created_at`
- `updated_at`

Those fields remain managed by the existing auditing policy.

The addition of `is_deleted` must not alter current audit timestamp behavior.

---

## Alternatives Considered

### Option A

Add `is_deleted` to `BaseEntity` and adopt a project-wide soft delete baseline for all JPA entities.

Pros:

* creates one explicit deletion standard across the project
* prevents inconsistent per-entity deletion field naming
* gives future entities a consistent lifecycle baseline
* supports safer synchronization and operational workflows by avoiding default physical deletion
* aligns deletion policy with the existing shared `BaseEntity` model

Cons:

* requires schema and entity updates across all current and future tables
* introduces a common field even for entities that may rarely use deletion semantics
* requires disciplined query filtering to avoid exposing deleted rows
* snapshot/history/versioned tables inherit a field they may not actively use in normal workflows

### Option B

Do not add `is_deleted` to `BaseEntity`, and add soft delete only to selected entities.

Pros:

* minimizes broad schema changes
* avoids adding deletion state to tables that may never need it
* allows table-specific lifecycle design

Cons:

* weakens project-wide consistency
* pushes deletion-policy decisions into repeated local design work
* risks inconsistent naming and repository behavior
* makes future synchronization rules less uniform

### Option C

Do not introduce `is_deleted`, and use `is_active` to represent both inactive and deleted states.

Pros:

* smallest implementation change
* avoids an additional common column
* can work for simple activation-based visibility control

Cons:

* mixes business activation state with logical deletion state
* makes repository deletion policy ambiguous
* weakens clarity for synchronization workflows
* does not provide a real common deletion standard
* creates long-term ambiguity for recovery, auditing, and historical analysis

---

## Consequences

Positive:

* the project gains one explicit deletion standard for all JPA entities
* future entities inherit a consistent logical deletion baseline
* synchronization workflows can avoid default physical delete behavior
* `is_active` and `is_deleted` are clearly separated as business-state versus deletion-state
* repository deletion behavior becomes more predictable
* schema and entity conventions remain aligned through one shared base structure

Negative:

* all entities and schema documentation must be updated to accommodate `is_deleted`
* normal query paths must be reviewed to ensure deleted rows are excluded
* some tables will structurally inherit `is_deleted` even if they rarely use it operationally
* snapshot/history/versioned workflows require discipline so soft delete does not replace historical preservation
* implementation complexity increases for repository filtering and synchronization semantics

---

## Related Plans

* PLAN-0008: Football DNA Data Database Synchronization (expected)
* PLAN-0009: Club Domain Implementation for Normalized Football DNA Data (expected)
* PLAN-0010: BaseEntity Soft Delete and Schema/Convention Alignment (expected)

---

## Change History

### 2026-06-11

* Decision created
* Status set to Proposed

### 2026-06-11

* Decision approved by user
* Status set to Accepted
