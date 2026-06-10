# PLAN-0005: Football DNA Data Import Package Restructure

## Metadata

| Field | Value |
| --- | --- |
| Plan ID | PLAN-0005 |
| Title | Football DNA Data Import Package Restructure |
| Type | Refactor |
| Status | Completed |
| Created At | 2026-06-10 |
| Updated At | 2026-06-10 |

---

## Goal

Move the existing Football DNA Data Import implementation from `com.kopite.fd.admin` into the `com.kopite.fd.admin.dataimport` package structure defined in `PACKAGE_STRUCTURE.md` without changing behavior.

---

## Scope

This Plan includes:

* reviewing the latest `docs/architecture/PACKAGE_STRUCTURE.md`
* moving only Football DNA Data Import related production classes from `com.kopite.fd.admin` into `com.kopite.fd.admin.dataimport`
* updating package declarations and imports required by the package move
* mirroring the same package structure changes in the related test classes
* splitting the current `infrastructure.adapter` implementations into:
  * `infrastructure.datasource`
  * `infrastructure.parser`
  * `infrastructure.converter`
  * `infrastructure.artifact`
* preserving the current API behavior and response structure
* verifying that no Football DNA Data Import class remains directly under `com.kopite.fd.admin`
* running the existing test suite to confirm behavior is unchanged

Target structure:

```text
admin
└─ dataimport
   ├─ controller
   ├─ dto
   │  └─ response
   ├─ application
   │  ├─ model
   │  ├─ result
   │  └─ service
   ├─ domain
   │  ├─ repository
   │  └─ type
   └─ infrastructure
      ├─ datasource
      ├─ parser
      ├─ converter
      └─ artifact
```

---

## Out of Scope

This Plan does not include:

* any behavior change to the Football DNA Data Import flow
* any API path or DTO field change
* any runtime verification work
* any recommendation logic change
* any database synchronization change
* any new import feature
* renaming classes unless required by package movement
* moving unrelated `admin` functionality

---

## Tasks

### Phase 1

* [x] Review the current Football DNA Data Import package layout against `PACKAGE_STRUCTURE.md`
* [x] Identify the exact production and test classes that belong to the import feature
* [x] Define the target package destinations under `admin.dataimport`

### Phase 2

* [x] Move production classes into `com.kopite.fd.admin.dataimport`
* [x] Split infrastructure implementations into `datasource`, `parser`, `converter`, and `artifact`
* [x] Update package declarations and imports across production code

### Phase 3

* [x] Move related test classes to mirror the new production package structure
* [x] Update package declarations and imports across test code
* [x] Verify no import-related production class remains directly under `com.kopite.fd.admin`
* [x] Verify no API behavior changed as a result of the package move

### Phase 4

* [x] Run `./gradlew test`
* [x] Record validation results
* [x] Update Plan status when complete

---

## Dependencies

Related Plans (Optional):

- PLAN-0004

Related Decisions (Optional):

- DECISION-0003

---

## Progress Log

### 2026-06-10

* Plan created
* Status set to Proposed

### 2026-06-10

* Plan approved by user
* Status set to In Progress

### 2026-06-10

* Moved Football DNA Data Import production classes under `com.kopite.fd.admin.dataimport`
* Split import infrastructure implementations into `datasource`, `parser`, `converter`, and `artifact`
* Moved related test classes to mirror the new `admin.dataimport` production package layout
* Verified no Football DNA Data Import production class remains directly under `com.kopite.fd.admin`
* Verified API behavior remained unchanged by preserving the existing controller path and DTO contract
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Status set to Completed

---
## Validation

Describe how this Plan should be verified.

- Lint/static checks: compile checks covered through `./gradlew test`
- Unit tests: existing import-related unit tests remain green after package movement
- Integration tests: existing import orchestration tests remain green after package movement
- Manual verification: inspect the package layout and imports to confirm Football DNA Data Import classes now live under `com.kopite.fd.admin.dataimport`
- Validation result: verified import-related production classes now exist only under `com.kopite.fd.admin.dataimport`; verified existing controller path `/api/admin/football-dna-data/import` remains unchanged

---

## Completion Criteria

This Plan is considered complete when:

- [x] All tasks are completed
- [x] Football DNA Data Import production classes are moved under `com.kopite.fd.admin.dataimport`
- [x] Related test classes mirror the new production package structure
- [x] `infrastructure.adapter` implementations are split into `datasource`, `parser`, `converter`, and `artifact`
- [x] No import-related class remains directly under `com.kopite.fd.admin`
- [x] Existing API behavior is unchanged
- [x] Scope requirements are satisfied
- [x] Lint or static checks pass
- [x] Related tests pass
- [x] Validation results are recorded
- [x] Plan status updated in `docs/plans/README.md`
