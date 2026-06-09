# DECISION-0001: Assessment Aggregate, Identity and Lifecycle Strategy

## Metadata

| Field | Value |
| --- | --- |
| Decision ID | DECISION-0001 |
| Title | Assessment Aggregate, Identity and Lifecycle Strategy |
| Status | Accepted |
| Created At | 2026-06-09 |
| Updated At | 2026-06-09 |

---

## Context

Football DNA requires an assessment flow that collects user responses, generates Emotional DNA and Playstyle DNA, and then passes a stable assessment result into recommendation generation.

The current project resources define:

- an assessment-first recommendation flow in `PROJECT_OVERVIEW.md`
- an assessment-centered persistence model in `DATABASE_SCHEMA.md`
- versioned recommendation-related club data sourced from Football DNA Data in `FOOTBALL_DNA_DATA.md`

Before backend implementation begins, the project needs a clear decision for:

- the aggregate boundary of an assessment
- how an assessment is identified
- what lifecycle states are required
- whether DNA scores are persisted or derived on demand
- when question, algorithm, and club data versions are frozen

These choices are difficult to reverse because they affect domain boundaries, persistence design, API behavior, and determinism of future recommendation results.

---

## Decision

### 1. Aggregate Boundary

`user_assessments` is the aggregate root for the Assessment domain.

The assessment consistency boundary includes:

- `user_assessments`
- `assessment_answers`
- `assessment_dna_scores`

Within this boundary, the system must preserve consistency for:

- assessment identity
- lifecycle status
- assessment timestamps
- frozen assessment and recommendation version references
- submitted answers
- finalized DNA score snapshot

The following are outside the assessment aggregate and are treated as reference data or downstream results:

- `questions`
- `question_options`
- `option_score_mappings`
- `dna_definitions`
- `clubs`
- `club_dna_scores`
- `club_tags`
- `assessment_recommendations`
- `assessment_ai_adjustments`

### 2. Assessment Identity Strategy

Each assessment must have at least one identity reference.

Validation rule:

- at least one of `user_id` or `anonymous_id` must be present

MVP identity behavior:

- authenticated assessments may use `user_id`
- anonymous assessments may use `anonymous_id`
- both fields may be present if future account-linking workflows require it, but the current decision does not define account-linking behavior

### 3. Assessment Lifecycle

The required MVP lifecycle states are:

- `IN_PROGRESS`
- `COMPLETED`

Lifecycle rules:

- an assessment starts in `IN_PROGRESS`
- answers may be submitted only while the assessment is `IN_PROGRESS`
- DNA scores become finalized when the assessment transitions to `COMPLETED`
- recommendation generation may use only `COMPLETED` assessments

Future lifecycle expansion, such as `ABANDONED`, is allowed if later product or operational needs justify it.

### 4. DNA Score Persistence Strategy

DNA scores must be persisted in `assessment_dna_scores` as a finalized snapshot.

Persistence model:

- `assessment_answers` records the raw user responses
- the system calculates assessment DNA from those responses
- when the assessment is completed, the calculated DNA scores are stored in `assessment_dna_scores` as the official assessment result snapshot

This persisted score snapshot becomes the stable handoff from Assessment to Recommendation.

### 5. Version Freezing Policy

`question_version` is frozen when the assessment starts.

Reason:

- the user must complete the assessment against a stable question set

`algorithm_version` is frozen when the assessment is completed and DNA scores are finalized.

Reason:

- the finalized DNA output must record the scoring logic that produced it

`club_data_version` is not frozen at assessment start or completion.

`club_data_version` is frozen when recommendation generation occurs for the assessment.

Reason:

- club data belongs to recommendation input data, not to question answering itself
- recommendation data is governed by the synchronized Football DNA dataset

### 6. Recommendation Boundary

Recommendation generation must treat a completed assessment as its stable input boundary.

That input boundary consists of:

- a `COMPLETED` assessment
- frozen `question_version`
- frozen `algorithm_version`
- finalized `assessment_dna_scores`

When recommendations are generated, the system must additionally freeze the `club_data_version` used for that run.

---

## Alternatives Considered

### Option A

Do not persist `assessment_dna_scores` and always calculate DNA on demand from `assessment_answers`.

Pros:

- avoids duplicated score storage
- uses answers as the only persisted assessment output

Cons:

- makes historical result reproduction depend on recomputation rules
- weakens deterministic recommendation inputs if scoring logic evolves
- does not align well with the existing schema, which already includes `assessment_dna_scores`

### Option B

Persist `assessment_dna_scores` as a finalized assessment snapshot and treat `user_assessments` as the aggregate root.

Pros:

- aligns with the existing schema
- creates a stable handoff into recommendation generation
- supports reproducible historical recommendation results
- keeps assessment lifecycle, answers, and final score output under one consistency boundary

Cons:

- duplicates data derivable from answers
- requires consistency rules for finalization timing

### Option C

Split identity, lifecycle, and versioning into separate Decisions before implementation.

Pros:

- smaller individual decisions
- potentially more focused discussion for each topic

Cons:

- introduces fragmentation for one tightly coupled aggregate
- increases the risk of inconsistent rules across related decisions
- does not match the immediate implementation need for one coherent assessment model

### Option D

Freeze `question_version`, `algorithm_version`, and `club_data_version` all at assessment start.

Pros:

- strongest early freezing model
- simple to describe

Cons:

- treats club data as part of the assessment interaction even though it is recommendation input
- may freeze downstream data before recommendation generation actually occurs

---

## Consequences

Positive:

- assessment implementation can proceed with a clear aggregate root and persistence boundary
- recommendation generation receives a stable, versioned assessment input
- the current schema is used in a way that supports deterministic and reproducible behavior
- anonymous and authenticated assessments are both supported by the domain model

Negative:

- persisted DNA scores introduce duplicated derived data
- allowing at least one of `user_id` or `anonymous_id` without defining full account-linking behavior leaves a future refinement area
- keeping only `IN_PROGRESS` and `COMPLETED` may require later lifecycle expansion if incomplete-session handling becomes important

---

## Change History

### 2026-06-09

- Decision created
- Status set to Accepted
