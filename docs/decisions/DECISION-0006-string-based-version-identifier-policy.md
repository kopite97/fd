# DECISION-0006: String-Based Version Identifier Policy

## Metadata

| Field         | Value |
|---------------|-------|
| Decision ID   | DECISION-0006 |
| Title         | String-Based Version Identifier Policy |
| Status        | Accepted |
| Superseded By |  |
| Created At    | 2026-06-11 |
| Updated At    | 2026-06-11 |

---

## Context

The project previously treated some version-related fields as numeric version counters.

That assumption is no longer valid.

The project has finalized a unified string-based version identifier policy for the following fields:

- `question_version`
- `algorithm_version`
- `club_data_version`
- `club_dna_scores.data_version`

The latest `DATABASE_SCHEMA.md` has been updated to model these fields as `VARCHAR(20)`.

These values are used for:

- snapshot reproducibility
- auditability
- historical result reconstruction
- version traceability

These values are not intended for numeric comparison or arithmetic ordering.

This Decision is required because the version identifier policy affects:

- database schema definitions
- JPA entity field types
- persistence mapping rules
- assessment lifecycle behavior
- recommendation reproducibility
- the relationship between assessment-side version references and club data snapshots

This Decision also clarifies and partially overrides earlier integer-based assumptions recorded in `DECISION-0004`.

---

## Decision

### 1. Decision Summary

The project will use string-based version identifiers for all finalized version reference fields listed below:

- `user_assessments.question_version`
- `user_assessments.algorithm_version`
- `user_assessments.club_data_version`
- `club_dna_scores.data_version`

These fields must be modeled as `VARCHAR(20)` in the database and `String` in application and JPA layers.

### 2. Rationale

The selected fields represent named historical references, not numeric counters.

The project must preserve:

- which question set was used
- which recommendation algorithm version produced a result
- which club data snapshot was used
- which club DNA snapshot rows belong to the same synchronized dataset

String identifiers are preferred because they:

- express domain meaning explicitly
- improve traceability in logs, artifacts, and persisted records
- avoid treating historical snapshot references as arithmetic values
- align with the already adopted identifier style for `question_version`

### 3. Version Identifier Naming Convention

The standard naming convention is:

- `question_version = q-v1`
- `algorithm_version = alg-v1`
- `club_data_version = club-v1`
- `club_dna_scores.data_version = club-v1`

Rules:

- identifiers must be stable once introduced
- identifiers must fit within `VARCHAR(20)`
- identifiers must not rely on numeric comparison semantics
- prefixes should remain domain-specific so different version namespaces cannot be confused

### 4. Lifecycle Rules

#### question_version

- assigned when an assessment starts
- frozen at assessment start
- must remain unchanged for the lifetime of the assessment

#### algorithm_version

- assigned when an assessment is completed
- frozen when DNA scores are finalized
- must identify the scoring logic that produced the finalized assessment result

#### club_data_version

- not assigned at assessment start
- not assigned at assessment completion
- assigned when recommendation generation occurs
- frozen when recommendation generation uses a specific club dataset snapshot

#### club_dna_scores.data_version

- assigned during a successful club data synchronization run
- all `club_dna_scores` rows created from the same synchronized snapshot must share the same `data_version`
- must remain immutable once persisted

### 5. Database Rules

The following fields must use `VARCHAR(20)`:

- `user_assessments.question_version`
- `user_assessments.algorithm_version`
- `user_assessments.club_data_version`
- `club_dna_scores.data_version`

Nullability rules must follow lifecycle timing:

- `question_version`: `NOT NULL`
- `algorithm_version`: nullable before assessment completion, non-null after completion
- `club_data_version`: nullable before recommendation generation, non-null once recommendation data is frozen
- `club_dna_scores.data_version`: `NOT NULL`

Default rules:

- `question_version` may use a documented default such as `q-v1` only if the application explicitly intends to use that as the default active question set
- `algorithm_version` should not rely on a database default, because it is assigned at completion time
- `club_data_version` should not rely on a database default, because it is assigned at recommendation time
- `club_dna_scores.data_version` should not rely on a database default, because it must reflect the actual synchronized dataset version chosen by the application

### 6. Relationship Between Assessment and Club Data Versions

`user_assessments.club_data_version` and `club_dna_scores.data_version` belong to the same version namespace.

Meaning:

- `user_assessments.club_data_version` stores the club dataset identifier frozen for a recommendation run
- `club_dna_scores.data_version` stores the dataset identifier attached to persisted club DNA snapshot rows
- recommendation generation must use `club_dna_scores` rows whose `data_version` exactly matches the frozen `user_assessments.club_data_version`

Example:

- assessment recommendation freezes `club_data_version = club-v1`
- recommendation reads only `club_dna_scores` rows where `data_version = club-v1`

### 7. Impact on Existing Decisions

#### DECISION-0001

This Decision is consistent with `DECISION-0001` on freeze timing:

- `question_version` frozen at assessment start
- `algorithm_version` frozen at assessment completion
- `club_data_version` frozen at recommendation generation

This Decision adds type and identifier-format rules that `DECISION-0001` did not define.

#### DECISION-0004

This Decision partially overrides the version-field type assumption in `DECISION-0004`.

`DECISION-0004` stated that schema-defined `INT` version fields should map to `Integer`.

That rule no longer applies to the following fields:

- `question_version`
- `algorithm_version`
- `club_data_version`
- `club_dna_scores.data_version`

For these fields, the new standard is:

- database type: `VARCHAR(20)`
- Java/JPA type: `String`

All remaining non-version `INT` schema fields remain governed by `DECISION-0004`.

### 8. Migration Considerations

PostgreSQL schema alignment is still required where the live database differs from the documented schema.

Migration review must verify:

- column length alignment to `VARCHAR(20)`
- removal of outdated integer-based assumptions
- nullable/default settings that currently conflict with lifecycle behavior
- whether any existing values exceed length or use outdated naming conventions

No numeric-to-string semantic conversion is required where live columns already use string types.

However:

- existing null data may remain valid before freeze points
- database constraints must not force premature assignment of `algorithm_version` or `club_data_version`

### 9. Consequences

Positive:

- version semantics become explicit and stable
- assessment and recommendation reproducibility improve
- persistence mappings become easier to reason about
- logs and stored data become more interpretable
- `club_data_version` and `club_dna_scores.data_version` become directly relatable

Negative:

- existing JPA mappings and persistence conversion code must be updated
- PostgreSQL schema still needs alignment with the documented policy
- some current schema defaults and nullability assumptions must be revised
- `DECISION-0004` requires clarification through this Decision

---

## Alternatives Considered

### Option A

Keep `algorithm_version`, `club_data_version`, and `club_dna_scores.data_version` as numeric version counters.

Pros:

* simple numeric progression
* easy ordering in SQL

Cons:

* weak domain expressiveness
* conflicts with existing identifier-style usage such as `alg-v1`
* encourages treating historical references as arithmetic values

### Option B

Use string identifiers only for assessment fields and keep `club_dna_scores.data_version` as `INT`.

Pros:

* minimizes database change for club snapshot rows
* can preserve existing numeric sync counters

Cons:

* splits one logical version namespace into two representations
* complicates recommendation snapshot matching
* weakens consistency between frozen assessment version references and club data snapshot rows

### Option C

Use unified string-based identifiers for all related version reference fields.

Pros:

* one coherent version namespace
* clearer auditability and reproducibility
* aligns better with named historical snapshots

Cons:

* requires coordinated schema and JPA updates
* requires explicit lifecycle/nullability clarification

---

## Consequences

Positive:

* one unified version identifier policy across assessment and club data snapshots
* clearer lifecycle ownership of version assignment
* direct matching between frozen recommendation inputs and synchronized club data

Negative:

* follow-up schema migration and code alignment work is required
* historical Plans and older assumptions remain in the repository and must be read as superseded context

---

## Related Plans

* PLAN-0010 (expected): Version Identifier Policy Alignment Fix
* PLAN-0011 (expected): Football DNA Data Database Synchronization
* PLAN-0012 (expected): Recommendation Domain Version Freeze Implementation

---

## Change History

### 2026-06-11

* Decision created
* Status set to Accepted
