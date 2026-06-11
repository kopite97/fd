# PLAN-0007: Backend Architecture and Convention Alignment Refactor

## Metadata

| Field      | Value                                                             |
| ---------- | ----------------------------------------------------------------- |
| Plan ID    | PLAN-0007                                                         |
| Title      | Backend Architecture and Convention Alignment Refactor            |
| Type       | Refactor                                                          |
| Status     | Completed                                                         |
| Created At | 2026-06-10                                                        |
| Updated At | 2026-06-10                                                        |

---

## Goal

Refactor existing backend code so it aligns with the latest backend architecture and coding convention documents without changing business behavior.

---

## Scope

This Plan includes:

* reviewing `src/main/java/com/kopite/fd/AGENTS.md`, `docs/architecture/AGENTS.md`, and the referenced backend architecture documents before implementation
* correcting convention violations in existing backend code where the refactor is local, deterministic, and behavior-preserving
* aligning package placement where the required move is clear from the current architecture documents and does not require redesigning domain boundaries
* aligning JPA entities with `ENTITY_CONVENTIONS.md`, including `BaseEntity` inheritance, explicit column metadata, wrapper/decimal/date types, and audit ownership
* aligning controller and DTO structure with `API_CONVENTIONS.md` where this does not change public API behavior
* aligning Spring bean construction and Lombok usage with `CODING_CONVENTIONS.md`
* updating tests, imports, and package declarations as needed to keep the current backend behavior intact

---

## Out of Scope

This Plan does not include:

* new features
* DB import persistence
* recommendation logic
* redesigning assessment, DNA, user, club, or recommendation domain boundaries
* moving code into newly split domains when the required ownership change is architectural rather than local refactoring
* API contract changes beyond behavior-preserving convention fixes
* introducing new dependencies

---

## Tasks

### Phase 1

* [x] Re-review the latest backend architecture and convention documents and record the applicable rules for the touched code
* [x] Inventory existing convention violations in controllers, services, entities, repositories, configuration classes, and tests
* [x] Separate violations into:
  * [x] safe local refactors that can be completed in this Plan
  * [x] larger ownership or boundary issues that require a follow-up Plan or Decision

### Phase 2

* [x] Refactor Spring-managed classes to follow constructor injection and Lombok conventions where this is behavior-preserving
* [x] Refactor API layer code to follow controller/DTO conventions without changing endpoint behavior
* [x] Refactor JPA entities to follow entity conventions, including explicit column metadata and schema-aligned field declarations
* [x] Move classes only where package ownership is clear and the move does not require architectural redesign
* [x] Keep dependency direction aligned with `BACKEND_ARCHITECTURE.md`

### Phase 3

* [x] Update affected tests and package declarations to match the refactored structure
* [x] Run `./gradlew test`
* [x] Run `./gradlew check`
* [x] Record any remaining violations that were intentionally deferred because they exceed safe refactor scope

---

## Dependencies

Related Plans (Optional):

- PLAN-0005
- PLAN-0006

Related Decisions (Optional):

- DECISION-0003
- DECISION-0004

---

## Progress Log

### 2026-06-10

* Plan created

### 2026-06-10

* Plan approved by user
* Spring-managed constructors aligned with Lombok constructor-injection conventions
* JPA entity annotations aligned with entity conventions for touched entities
* Deferred cross-domain ownership moves that require a separate Plan or Decision
* Validation completed with `./gradlew test` and `./gradlew check`

---
## Validation

Describe how this Plan should be verified.

- Lint/static checks: `./gradlew check` passed on 2026-06-10
- Unit tests: `./gradlew test` passed on 2026-06-10
- Integration tests: existing Spring Boot test suite passed through `./gradlew test`
- Manual verification: no direct Spring bean constructors remain for repository/service/provider/property wiring; no `@Data` or `@Autowired` usage remains in main backend code

Deferred follow-up:

- `dna_definitions` ownership is defined under the `dna` package in `PACKAGE_STRUCTURE.md`, but moving the existing DNA model, repository, entity, and persistence adapter out of `assessment` changes package ownership boundaries and should be handled by a separate Plan or Decision.
- `user_assessments.algorithm_version` and `user_assessments.club_data_version` are nullable in the current code path because new assessments are created before completion or club data persistence exists. Full schema nullability/default alignment should be handled separately to avoid changing assessment start behavior in this refactor.

---

## Completion Criteria

This Plan is considered complete when:

- [x] All tasks are completed
- [x] Scope requirements are satisfied
- [x] Safe convention violations identified for this Plan are fixed
- [x] Remaining large-scope violations are explicitly documented for follow-up
- [x] Backend dependency direction remains consistent with `BACKEND_ARCHITECTURE.md`
- [x] Entity implementations align with `ENTITY_CONVENTIONS.md` for all touched entities
- [x] Controller and DTO implementations align with `API_CONVENTIONS.md` for all touched APIs
- [x] Lombok and Spring bean usage align with `CODING_CONVENTIONS.md` for all touched classes
- [x] `./gradlew test` passes
- [x] `./gradlew check` passes
- [x] Validation results are recorded
- [x] Plan status updated in `docs/plans/README.md`
