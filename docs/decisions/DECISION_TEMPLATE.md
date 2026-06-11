# DECISION-XXXX: Title

## Metadata

| Field         | Value                                       |
|---------------|---------------------------------------------|
| Decision ID   | DECISION-XXXX                               |
| Title         |                                             |
| Status        | Proposed / Accepted / Rejected / Superseded |
| Superseded By | DECISION-YYYY / PLAN-YYYY  (Optional)       |
| Created At    | YYYY-MM-DD                                  |
| Updated At    | YYYY-MM-DD                                  |

When a Decision status is changed to `Superseded`:

* `Superseded By` must reference the replacing Decision.
* The replacing Decision should reference the superseded Decision in its Context section.
* A superseded Decision must remain in the repository for historical traceability.
* Do not delete superseded Decisions.
* The 'Superseded By' can contain DECISION or PLAN.

---

## Context

Describe the problem, requirement, or situation that requires a decision.

Example:

The recommendation engine requires a similarity calculation method to compare user DNA and club DNA.

---

## Decision

Describe the selected decision.

Example:

Use Cosine Similarity as the primary similarity calculation method.

---

## Alternatives Considered

### Option A

Description.

Pros:

* Item 1
* Item 2

Cons:

* Item 1

### Option B

Description.

Pros:

* Item 1
* Item 2

Cons:

* Item 1

---

## Consequences

Describe the expected impact of this decision.

Positive:

* Item 1
* Item 2

Negative:

* Item 1

---

## Related Plans

* PLAN-XXXX

---

## Change History

### YYYY-MM-DD

* Decision created

### YYYY-MM-DD

* Status changed to Accepted
