# PLAN-0002: Assessment Package Structure Reorganization

## Metadata

| Field | Value |
| --- | --- |
| Plan ID | PLAN-0002 |
| Title | Assessment Package Structure Reorganization |
| Type | Refactor |
| Status | Completed |
| Created At | 2026-06-10 |
| Updated At | 2026-06-10 |

---

## Goal

Reorganize the Assessment domain production and test package structure to match the latest `PACKAGE_STRUCTURE.md` without changing behavior, API contracts, persistence behavior, or test expectations.

---

## Scope

This Plan includes:

* reorganizing `src/main/java/com/kopite/fd/assessment` to match the latest target structure
* reorganizing `src/test/java/com/kopite/fd/assessment` to mirror the updated production structure where applicable
* updating package declarations and imports only as required by file moves
* preserving all existing runtime behavior
* preserving all existing HTTP endpoints and request/response shapes
* preserving all existing test behavior
* re-running verification after reorganization

Target structure:

```text
assessment
├─ controller
├─ dto
│  ├─ request
│  └─ response
├─ application
│  ├─ command
│  ├─ query
│  ├─ result
│  └─ service
├─ domain
│  ├─ model
│  ├─ repository
│  └─ type
└─ infrastructure
   ├─ adapter
   ├─ entity
   └─ repository
```

`mapper` remains optional and is not required unless necessary to complete the reorganization cleanly.

---

## Out of Scope

This Plan does not include:

* any behavior change
* any API contract change
* any database schema change
* any new endpoint
* any new validation rule
* any new domain rule
* any new persistence rule
* extracting new business logic
* introducing recommendation logic
* introducing AI refinement
* reorganizing domains outside `assessment`
* converting embedded entity mapping methods into dedicated mapper classes unless required by the move itself

---

## Tasks

### Phase 1

* [x] Move application commands into `application/command`
* [x] Move application queries into `application/query`
* [x] Move application results into `application/result`
* [x] Move application services into `application/service`

### Phase 2

* [x] Move domain models into `domain/model`
* [x] Move domain repository interfaces into `domain/repository`
* [x] Move domain types into `domain/type`

### Phase 3

* [x] Move infrastructure entities into `infrastructure/entity`
* [x] Move infrastructure Spring Data repositories into `infrastructure/repository`
* [x] Move persistence adapters into `infrastructure/adapter`

### Phase 4

* [x] Reorganize assessment test packages to mirror the updated production structure
* [x] Update package declarations and imports across production and test code
* [x] Verify no endpoint paths or DTO field names changed
* [x] Verify all moved files conform to PACKAGE_STRUCTURE.md

### Phase 5

* [x] Run `./gradlew test`
* [x] Record verification results
* [x] Update `docs/plans/README.md` to reflect the final Plan status

---

## Dependencies

Related Plans (Optional):

- PLAN-0001

Related Decisions (Optional):

- DECISION-0001

---

## Progress Log

### 2026-06-10

* Plan created
* Status set to Approved

### 2026-06-10 Verification

* Verified assessment production packages match `PACKAGE_STRUCTURE.md`
* Verified assessment test packages mirror the updated production structure
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Verified `compileJava` and `compileTestJava` passed through the test run
* Verified existing assessment tests remain green after package reorganization
* Status set to Completed

---
## Validation

Describe how this Plan should be verified.

- Lint/static checks: compile checks covered through `./gradlew test`
- Unit tests: existing assessment application and behavior tests remain green
- Integration tests: existing persistence-related tests remain green
- API tests: existing controller tests remain green
- Manual verification: review diff to confirm changes are structural only

If test failures occur:

- Fix only package/import related failures.
- Do not change behavior to make tests pass.
- Re-run `./gradlew test` after fixes.
- Keep the Plan open until verification passes.

---

## Completion Criteria

This Plan is considered complete when:

- [x] Assessment production packages match `PACKAGE_STRUCTURE.md`
- [x] Assessment test packages mirror the updated production structure
- [x] Package declarations and imports are updated
- [x] Existing behavior is unchanged
- [x] Existing API contracts are unchanged
- [x] `./gradlew test` passes
- [x] Verification results are recorded
- [x] `docs/plans/README.md` is updated
