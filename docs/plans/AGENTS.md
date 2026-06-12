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

## Plan Workflow

When a new request is received:

1. Review `./README.md`
2. Check for an existing applicable Plan.
3. If an applicable Plan exists, continue using it.
4. If no applicable Plan exists:

    * Use `./PLAN_RUNTIME_TEMPLATE.md` when the work introduces APIs, external integrations, scheduled jobs, data import/export flows, or executable application behavior.
    * Otherwise use `./PLAN_TEMPLATE.md`.
5. Present the Plan to the user.
6. Wait for approval before implementation.

Do not begin implementation without an approved Plan.

---

## Plan Selection

If multiple applicable Plans exist:

1. Prefer the Plan explicitly referenced by the user.
2. Otherwise select the most relevant active Plan.
3. Do not work across multiple Plans unless explicitly requested.

---

## Plan Identification

Before implementation:

* Identify and reference the applicable Plan ID.
* Verify the requested work is within the selected Plan scope.
* If unclear, ask for clarification.

---

## Plan Execution

Once a Plan is approved, the agent may execute all tasks within the approved Plan scope without asking for approval after each task.

The agent must stop and ask for approval only when:

* Work falls outside the approved Plan scope
* A new Decision is required
* A conflict with an existing Decision or AGENTS.md rule is found
* The implementation requires introducing a new dependency, framework, or architectural pattern

Within the approved Plan scope, the agent should:

1. Implement the planned work.
2. Add required tests.
3. Run required verification checks.
4. Fix failures within scope.
5. Update task progress.
6. Update the Progress Log.
7. Keep Plan status synchronized with actual progress.
8. Complete the Plan when all completion requirements are satisfied.

---

## Plan Scope

Work only within the approved Plan scope.

If new work falls outside the current Plan:

1. Stop.
2. Create a new Plan or update the existing Plan.
3. Request approval.

Do not expand scope without approval.

---

## Plan Status

A Plan must use one of the following statuses:

* Draft — Initial work in progress
* Proposed — Waiting for user approval
* Approved — Approved but not started
* In Progress — Work has started
* Completed — Finished and verified
* Cancelled — No longer applicable

Rules:

* Implementation may only begin after user approval.
* When implementation begins, change Approved → In Progress.
* When all Completion Criteria are satisfied, change In Progress → Completed.
* Update `docs/plans/README.md` whenever Plan status changes.
* Completed and Cancelled Plans are historical records and should not be selected for new work.

---

## Task Progress Updates

When implementing work under an Approved Plan:

* Update task checkboxes as work is completed.
* Update the Progress Log when meaningful milestones are reached.
* Keep Plan status synchronized with actual implementation progress.

A separate user instruction is not required to update completed tasks.

However:

* Do not mark a task complete unless the implementation for that task exists.
* Do not mark a Plan Completed until all Completion Criteria are satisfied.

---

## Automatic Plan Completion

When all of the following are true:

* All Plan tasks are completed
* All Completion Criteria are satisfied
* Required tests pass
* Required verification checks pass
* Required runtime verification passes (when applicable)
* Validation results are recorded

The agent should automatically:

1. Update task checkboxes.
2. Update Completion Criteria checkboxes.
3. Update the Progress Log.
4. Change Plan status to Completed.
5. Update `docs/plans/README.md`.

A separate user approval is not required for Plan completion.

---

## Verification Requirement

Code generation alone is not sufficient.

Before a Plan may be marked Completed:

* Required tests must exist.
* Required tests must execute successfully.
* Required verification checks must execute successfully.
* Required runtime verification must execute successfully when the Plan introduces executable behavior, external integrations, scheduled jobs, APIs, or data import/export flows.
* Validation results must be recorded.

If verification cannot be executed:

* The Plan must remain In Progress.
* The Plan must not be marked Completed.

If runtime verification is required but cannot be executed:

* The Plan must remain In Progress.
* The missing runtime verification must be documented.

Examples:

* Tests were written but not executed → Not Completed
* Build compiles but tests were not executed → Not Completed
* Verification environment unavailable → Remain In Progress
* API implementation completed but endpoint was never called → Not Completed
* External integration implemented but runtime response was not verified → Not Completed
* Scheduled job implemented but never executed in a running application → Not Completed

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

* The work does not require Runtime Verification
* Creating a standard Feature, Fix, Refactor, or Test Plan
* Replacing an outdated standard Plan format

### PLAN_RUNTIME_TEMPLATE.md

Use `./PLAN_RUNTIME_TEMPLATE.md` when the work introduces:

* APIs
* External integrations
* Scheduled jobs
* Data import/export flows
* Executable application behavior

All runtime Plans should follow the runtime template unless explicitly instructed otherwise.

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

* ID must exactly match the document ID.
* Title must exactly match the document title.
* Status must reflect the current document status.

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

### Plan Numbering

Plan numbers must be assigned sequentially.

When creating a new Plan:

1. Find the highest existing Plan number.
2. Use the next available number.
3. Do not reuse numbers from deleted, cancelled, or completed Plans.
4. Preserve chronological ordering of Plan creation.

Examples:

```text
PLAN-0007-data-import.md
PLAN-0008-club-domain.md
PLAN-0009-recommendation-domain.md
```

If `PLAN-0009` is cancelled, the next Plan must still be:

```text
PLAN-0010-next-plan.md
```

Plan numbers represent creation order, not implementation order.


---

## Plan Structure

Standard Plans (`PLAN_TEMPLATE.md`) should contain:

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
* Validation
* Completion Criteria

Runtime Plans (`PLAN_RUNTIME_TEMPLATE.md`) should additionally contain:

* Runtime Verification

---

## Runtime Verification Requirements

Runtime Plans must explicitly define Runtime Verification steps.

The Runtime Verification section should describe:

* What will be executed
* What success looks like
* What outputs or side effects will be verified
* How the verification result will be recorded

### Runtime Execution Requirement

When Runtime Verification is required, the agent must verify behavior using a running application whenever the execution environment permits.

Examples:

* Start the application.
* Execute the implemented API.
* Verify the response.
* Verify relevant database state changes.
* Verify expected side effects.

Unit tests and integration tests alone do not satisfy Runtime Verification when the behavior can be validated through a running application.

If the application cannot be started due to environment limitations:

* The Plan must remain In Progress.
* The missing runtime verification must be documented.


---

## Plan Types

Available types:

* Feature
* Fix
* Refactor
* Test
* Design

---

## Design Plans

Design Plans are used when the primary deliverable is a design artifact rather than executable implementation.

Examples:

* Architecture design
* Synchronization mapping design
* API contract design
* Data model design
* Recommendation algorithm design

A Design Plan may be marked Completed when:

* The design artifact exists
* The design artifact has been reviewed
* Open questions are resolved or explicitly documented
* The design can be used as the baseline for future implementation Plans

Implementation of dependent Feature Plans is not required for Design Plan completion.