# Decisions

## Purpose

Decisions record important architectural, technical, and business choices.

A Decision describes:

* What was decided
* Why it was decided
* What alternatives were considered

Decisions exist to preserve project context and prevent repeated discussions.

---

## Decision Workflow

When an important decision is required:

1. Review `./README.md`.
2. Check for an existing applicable Decision.
3. If an applicable Decision exists, continue using it.
4. If no applicable Decision exists, create a new Decision using `./DECISION_TEMPLATE.md`.
5. Present the Decision to the user.
6. Wait for approval before proceeding.

Do not treat a Decision as project guidance until it has been approved.

---

## Decision Identification

Before implementation:

* Identify and reference the applicable Decision ID.
* Verify the requested work is consistent with the selected Decision.
* If unclear, ask for clarification.

---

## Decision Creation Criteria

Create a Decision when:

* Database structure changes
* Domain model changes
* Recommendation algorithm changes
* Architectural changes
* External integration changes
* Any decision that is difficult to reverse

Do not create Decisions for:

* Naming choices
* Method extraction
* Package organization
* Internal implementation details
* Minor refactoring decisions

---

## Decision References

The following documents should be used when working with Decisions.

### README.md

Use `./README.md` to:

* Discover existing Decisions
* Identify active Decisions
* Avoid creating duplicate Decisions

### DECISION_TEMPLATE.md

Use `./DECISION_TEMPLATE.md` when:

* Creating a new Decision
* Replacing an outdated Decision format

All new Decisions should follow the template unless explicitly instructed otherwise.

---

## README Maintenance

`./README.md` is the primary index for Decision discovery and status tracking.

Update `./README.md` whenever:

* A Decision is created
* A Decision status changes
* A Decision is superseded

README.md should always reflect the current state of all Decisions.

---

## README Consistency

Entries in `./README.md` must match the corresponding document.

- ID must exactly match the document ID.
- Title must exactly match the document title.
- Status must reflect the current document status.

---

## Decision Status

A Decision must use one of the following statuses:

* Proposed — Waiting for user approval
* Accepted — Approved and active
* Rejected — Explicitly rejected
* Superseded — Replaced by a newer Decision

Only Accepted Decisions may be used as project guidance.

Superseded Decisions remain as historical records.

---

## Decision Scope

Follow existing Accepted Decisions whenever applicable.

If a requested change conflicts with an Accepted Decision:

1. Stop.
2. Create a new Decision proposal.
3. Present the change to the user.
4. Wait for approval.

Do not override an Accepted Decision without approval.

---

## Decision Naming

File format:

```text
DECISION-0001-assessment-id.md
DECISION-0002-cosine-similarity.md
DECISION-0003-club-data-source.md
```

Decision IDs must be unique.

File names should remain stable after creation.

---

## Decision Structure

Each Decision should contain:

* Decision ID
* Title
* Status
* Context
* Decision
* Alternatives Considered
* Consequences
* Related Plans
