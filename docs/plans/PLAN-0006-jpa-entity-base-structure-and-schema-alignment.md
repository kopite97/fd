# PLAN-0006: JPA Entity Base Structure and Schema Alignment

## Metadata

| Field | Value |
| --- | --- |
| Plan ID | PLAN-0006 |
| Title | JPA Entity Base Structure and Schema Alignment |
| Type | Refactor |
| Status | Completed |
| Created At | 2026-06-10 |
| Updated At | 2026-06-10 |

---

## Goal

Align the project's JPA entity structure with the latest `DATABASE_SCHEMA.md` and `PACKAGE_STRUCTURE.md` by introducing a shared `BaseEntity`, enabling JPA auditing, removing duplicated audit fields, and updating existing entity field types and missing fields without changing business behavior.

---

## Scope

This Plan includes:

* reviewing the latest `docs/resources/DATABASE_SCHEMA.md`
* reviewing the latest `docs/architecture/PACKAGE_STRUCTURE.md`
* implementing `global.infrastructure.entity.BaseEntity`
* defining common audit fields in `BaseEntity`:
  * `createdAt`
  * `updatedAt`
* enabling JPA auditing if it is not already enabled
* updating existing JPA entity classes to inherit from `BaseEntity`
* removing duplicated audit fields from individual JPA entity classes
* aligning existing assessment JPA entity field types with `DECISION-0004`
* adding schema-defined fields that are currently missing from existing assessment JPA entities
* updating entity mapping code and related tests as required by structural and type changes
* running verification checks with `./gradlew test`
* running `./gradlew check` if the task exists in the project

---

## Out of Scope

This Plan does not include:

* changing business behavior
* implementing recommendation logic
* implementing DB import or persistence workflows outside entity structure alignment
* adding new dependencies
* creating empty packages
* introducing new non-entity domain features
* implementing new entity sets for domains that are not currently present in code unless required to satisfy an existing schema-alignment need already represented by current work

---

## Tasks

### Phase 1

* [x] Review current JPA entity classes against `DATABASE_SCHEMA.md`
* [x] Identify duplicated audit fields and schema mismatches in the existing assessment entity set
* [x] Confirm any required package ownership updates against `PACKAGE_STRUCTURE.md`

### Phase 2

* [x] Create `global.infrastructure.entity.BaseEntity`
* [x] Implement `createdAt` and `updatedAt` in `BaseEntity` using JPA auditing annotations
* [x] Enable JPA auditing if it is not already enabled

### Phase 3

* [x] Update existing JPA entity classes to inherit from `BaseEntity`
* [x] Remove duplicated audit fields from individual entity classes
* [x] Align schema-defined `INT` fields to `Integer`
* [x] Align schema-defined `DECIMAL(5,2)` fields to `BigDecimal`
* [x] Add currently missing schema-defined fields to existing assessment JPA entities

### Phase 4

* [x] Update entity mapping code affected by structural or type changes
* [x] Add or update tests as needed for entity mapping and persistence structure
* [x] Run `./gradlew test`
* [x] Run `./gradlew check` if available

### Phase 5

* [x] Record validation results
* [x] Update Plan status when complete

---

## Dependencies

Related Plans (Optional):

- PLAN-0001

Related Decisions (Optional):

- DECISION-0001
- DECISION-0004

---

## Progress Log

### 2026-06-10

* Plan created
* Status set to Proposed

### 2026-06-10

* Plan approved by user
* Status set to In Progress

### 2026-06-10

* Added `global.infrastructure.entity.BaseEntity` with shared `createdAt` and `updatedAt`
* Added conditional JPA auditing configuration under `global.infrastructure.config`
* Updated all existing assessment JPA entity classes to inherit from `BaseEntity`
* Removed duplicated audit fields from individual assessment entity classes
* Aligned schema-driven entity field types for existing assessment entities:
  * `question_version`, `algorithm_version`, `club_data_version` mapped as `Integer` in `AssessmentJpaEntity`
  * `question_version` mapped as `Integer` in `AssessmentQuestionJpaEntity`
  * `score` mapped as `BigDecimal` in `AssessmentDnaScoreJpaEntity`
  * `score_delta` mapped as `BigDecimal` in `OptionScoreMappingJpaEntity`
* Added missing `public_result_key` field to `AssessmentJpaEntity`
* Updated persistence mapping code to preserve existing domain behavior while using schema-aligned entity field types
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Executed `./gradlew check`
* Verification result: `BUILD SUCCESSFUL`
* Verified direct audit field declarations no longer remain in individual JPA entity classes
* Status set to Completed

---
## Validation

Describe how this Plan should be verified.

- Lint/static checks: compile and standard Gradle verification tasks applicable to the project
- Unit tests: entity mapping and conversion behavior affected by field-type or structure changes
- Integration tests: existing application and persistence tests remain green after entity inheritance and type alignment changes
- Manual verification: inspect entity classes to confirm audit fields are owned by `BaseEntity` and that existing assessment entities reflect schema-defined field additions and type alignment
- Validation result: verified `createdAt` and `updatedAt` are declared only in `global.infrastructure.entity.BaseEntity`; verified existing assessment entities inherit from `BaseEntity`; verified `./gradlew test` and `./gradlew check` both passed

---

## Completion Criteria

This Plan is considered complete when:

- [x] All tasks are completed
- [x] `global.infrastructure.entity.BaseEntity` exists and owns `createdAt` and `updatedAt`
- [x] Existing JPA entity classes inherit from `BaseEntity`
- [x] Duplicated audit fields are removed from individual JPA entity classes
- [x] Existing assessment JPA entity classes are aligned with `DECISION-0004` for schema-driven type choices
- [x] Missing schema-defined fields for the current assessment JPA entity set are added
- [x] Scope requirements are satisfied
- [x] Lint or static checks pass
- [x] Related tests pass
- [x] Validation results are recorded
- [x] Plan status updated in `docs/plans/README.md`
