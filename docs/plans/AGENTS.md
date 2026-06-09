# Plans

## Purpose

Plans define approved units of work.

A Plan describes:

* What will be built
* Why it is needed
* What is included
* What is excluded

All implementation work should be associated with a Plan.

---

## Plan Execution

Once a Plan is approved, the agent may execute all tasks within the approved Plan scope without asking for approval after each task.

The agent must stop and ask for approval only when:

- Work falls outside the approved Plan scope
- A new Decision is required
- A conflict with an existing Decision or AGENTS.md rule is found
- The implementation requires introducing a new dependency, framework, or architectural pattern

Within the approved Plan scope, the agent should:

1. Implement the planned work.
2. Add required tests.
3. Run required verification checks.
4. Fix failures within scope.
5. Update the Plan progress.
6. Mark the Plan Completed only when all completion criteria are satisfied.

---

## Plan Completion

A Plan may be marked as Completed only after required lint/static checks and related tests have passed.

---

## Plan Selection

If multiple applicable Plans exist:

1. Prefer the Plan explicitly referenced by the user.
2. Otherwise select the most relevant active Plan.
3. Do not work across multiple Plans unless explicitly requested.

---

## Plan Workflow

When a new request is received:

1. Review `./README.md`
2. Check for an existing applicable Plan.
3. If an applicable Plan exists, continue using it.
4. If no applicable Plan exists, create a new Plan using `./PLAN_TEMPLATE.md`.
5. Present the Plan to the user.
6. Wait for approval before implementation.

Do not begin implementation without an approved Plan.

---

## Plan Identification

Before implementation:

- Identify and reference the applicable Plan ID.
- Verify the requested work is within the selected Plan scope.
- If unclear, ask for clarification.

---

## Plan References

The following documents should be used when working with Plans.

### README.md

Use `./README.md` to:

* Discover existing Plans
* Identify active Plans
* Check Plan status
* Avoid creating duplicate Plans

### PLAN_TEMPLATE.md

Use `./PLAN_TEMPLATE.md` when:

* Creating a new Plan
* Replacing an outdated Plan format

All new Plans should follow the template unless explicitly instructed otherwise.

---

## README Maintenance

`./README.md` is the primary index for Plan discovery and status tracking.

Update `./README.md` whenever:

* A Plan is created
* A Plan status changes
* A Plan is completed
* A Plan is cancelled

`./README.md` should always reflect the current state of all Plans.

---

## README Consistency

Entries in `./README.md` must match the corresponding document.

- ID must exactly match the document ID.
- Title must exactly match the document title.
- Status must reflect the current document status.

---

## Plan Status

A Plan must use one of the following statuses:

* Draft — Initial work in progress
* Proposed — Waiting for user approval
* Approved — Approved but not started
* In Progress — Work has started
* Completed — Finished and accepted
* Cancelled — No longer applicable

Rules:
- Implementation may only begin after user approval.
- When implementation begins, change Approved → In Progress.
- When all Completion Criteria are satisfied, change In Progress → Completed.
- Update docs/plans/README.md whenever Plan status changes.
- Completed and Cancelled plans are historical records and should not be selected for new work.

---

## Plan Scope

Work only within the approved Plan scope.

If new work falls outside the current Plan:

1. Stop.
2. Create a new Plan or update the existing Plan.
3. Request approval.

Do not expand scope without approval.

---

## Plan Naming

File format:

```text
PLAN-0001-backend-architecture.md
PLAN-0002-user-assessment.md
PLAN-0003-recommendation-engine.md
```

Plan IDs must be unique.

File names should remain stable after creation.

---

## Plan Structure

Each Plan should contain:

* Plan ID
* Title
* Status
* Type
* Goal
* Scope
* Out of Scope
* Tasks
* Related Decisions
* Dependencies

---

## Task Progress Updates

When implementing work under an Approved Plan:

- Update task checkboxes as work is completed.
- Update the Progress Log when meaningful milestones are reached.
- Keep Plan status synchronized with actual implementation progress.

A separate user instruction is not required to update completed tasks.

However:

- Do not mark a task complete unless the implementation for that task exists.
- Do not mark a Plan Completed until all Completion Criteria are satisfied.

---

## Plan Types

Available types:

* Feature
* Fix
* Refactor
* Test


