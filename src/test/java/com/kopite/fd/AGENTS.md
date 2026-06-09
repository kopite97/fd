# Testing Rules

## Purpose

Tests exist to verify business behavior defined by accepted Decisions and approved Plans.

Tests should validate observable behavior, not implementation details.

---

## Test Scope

Prefer testing in the following order:

1. Domain behavior
2. Application use cases
3. Persistence integration
4. API behavior

Avoid testing framework internals or generated code.

---

## Test Implementation Rules

Prefer verifying behavior through public interfaces.

Do not test:

- private methods
- framework internals
- implementation details that are not observable behavior

Refactoring implementation details should not require rewriting tests unless behavior changes.

Favor testing business behavior over implementation structure.

---

## Unit Test Rules

Unit tests should:

* Focus on a single behavior.
* Have a clear Arrange / Act / Assert structure.
* Avoid unnecessary mocking.
* Be deterministic.
* Run without external infrastructure whenever possible.

Examples:

* Domain validation
* Lifecycle transitions
* Score calculation
* Business rules

---

## Integration Test Rules

Integration tests should verify:

* Repository persistence behavior
* Transaction boundaries
* Application service interactions
* Database mappings

Use integration tests when behavior cannot be verified through a unit test alone.

---

## API Test Rules

API tests should verify:

* Request validation
* Response structure
* HTTP status codes
* Endpoint wiring

Do not duplicate business-rule testing already covered by domain or application tests.

---

## Test Directory Structure

Test packages should mirror the production package structure.

Production:

```text
src/main/java/com/kopite/fd/assessment
```

Test:

src/test/java/com/kopite/fd/assessment

Domain tests should be placed near the corresponding domain package:

src/test/java/com/kopite/fd/assessment/domain

Application service tests should be placed near the corresponding application package:

src/test/java/com/kopite/fd/assessment/application

Infrastructure integration tests should be placed near the corresponding infrastructure package:

src/test/java/com/kopite/fd/assessment/infrastructure

Controller/API tests should be placed near the corresponding controller package:

src/test/java/com/kopite/fd/assessment/controller

Do not create test packages without test classes.

---

## Test Type Placement

Use the following placement by test type:

```text
domain
├── pure unit tests for domain models and business rules

application
├── unit tests for application services
├── use mocks or fakes for repositories when appropriate

infrastructure
├── persistence integration tests
├── JPA repository tests
├── database mapping tests

controller
├── API/controller tests
├── request validation tests
├── endpoint wiring tests
```

---

## Test Naming

Use descriptive test names.

Examples:

* shouldCreateAssessmentWhenIdentityIsValid
* shouldRejectAnswerSubmissionForCompletedAssessment
* shouldPersistFinalizedDnaScoresOnCompletion

---


## Test Data

Test data should:

* Be minimal.
* Be explicit.
* Be created inside the test whenever practical.

Avoid large shared fixtures unless reuse is justified.

---

## Plan Completion Requirements

A Plan cannot be marked Completed unless:

* Required unit tests pass.
* Required integration tests pass.
* Required API tests pass.
* Applicable lint/static checks pass.

If checks cannot be executed, clearly document the reason.

---

## Relationship To Plans And Decisions

Tests must verify behavior defined by:

1. Accepted Decisions
2. Approved Plans

Tests must not introduce new business rules.

If expected behavior is unclear, request clarification rather than making assumptions.

---

## Test Timing

When implementing a feature:

1. Production code may be created before tests.
2. Tests do not need to be written in the same implementation step.
3. Plan tasks may be implemented incrementally, but a task should not be marked complete until its required tests have been added.

A Plan must not be considered completed unless:

- Required tests have been added.
- Required tests pass.
- Required lint/static checks pass.
- Validation results are recorded.