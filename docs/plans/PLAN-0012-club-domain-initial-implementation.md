# PLAN-0012: Club Domain Initial Clubs Implementation

## Metadata

| Field      | Value                                                             |
| ---------- | ----------------------------------------------------------------- |
| Plan ID    | PLAN-0012                                                         |
| Title      | Club Domain Initial Clubs Implementation                          |
| Type       | Feature                                                           |
| Status     | Completed                                                         |
| Created At | 2026-06-11                                                        |
| Updated At | 2026-06-11                                                        |

---

## Goal

Implement the initial Club Domain based on the approved database schema, accepted Decisions, and the synchronization mapping produced by `PLAN-0011`.

The first Club Domain implementation is intentionally limited to `clubs` so the project can establish a low-risk persistence baseline before introducing unresolved source-policy questions from `club_tags` and `club_dna_scores`.

This Plan establishes the first club persistence baseline for future synchronization and recommendation work without implementing synchronization or recommendation behavior.

---

## Scope

This Plan includes:

* implementing the `club` domain package defined by `PACKAGE_STRUCTURE.md`
* implementing persistence support for `clubs`
* implementing a JPA entity aligned with `DATABASE_SCHEMA.md` for `clubs`
* implementing a club domain model for `clubs`
* implementing a domain repository interface for `clubs` persistence and retrieval
* implementing a persistence adapter and Spring Data JPA repository for `clubs`
* implementing application services for internal `clubs` read and persistence use cases
* implementing command/query/result models only where they are needed by `clubs` application services
* implementing tests for `clubs` entity mapping, repository behavior, adapter behavior, and application services
* implementing runtime verification that the application boots and the `clubs` persistence wiring functions correctly

The initial implementation scope is limited to:

* `clubs`

---

## Out of Scope

This Plan does not include:

* `club_tags`
* `club_dna_scores`
* Google Sheet synchronization
* Football DNA Data import orchestration changes
* synchronization transaction policy
* synchronization replace/upsert/delete behavior
* Recommendation Domain implementation
* Recommendation Engine logic
* Assessment Domain changes
* Import APIs
* scheduled synchronization
* club-status normalization dictionary implementation
* derivation rules for missing source fields such as:
  * `clubs.short_name`
  * `clubs.beginner_accessibility`
* `club_tags.tag_type` policy
* `club_dna_scores.is_core` policy
* rubric persistence for `dna-rubric`

---

## Tasks

### Phase 1

* [x] Review the latest AGENTS, accepted Decisions, active Plan index, `DATABASE_SCHEMA.md`, `PACKAGE_STRUCTURE.md`, and `FOOTBALL_DNA_DATA_SYNC_MAPPING.md`
* [x] Confirm the Club Domain ownership boundary and reduce the first implementation scope to `clubs`
* [x] Confirm which `clubs` behaviors are implementable now versus explicitly deferred

### Phase 2

* [x] Implement the club domain model for `clubs`
* [x] Implement the club domain repository interface for `clubs` read and persistence operations
* [x] Implement any required club-domain types needed to express current `clubs` schema fields clearly

### Phase 3

* [x] Implement the JPA entity for `clubs`
* [x] Implement the Spring Data JPA repository for `clubs`
* [x] Implement the persistence adapter that satisfies the club domain repository interface
* [x] Align the `clubs` persistence mapping with `DATABASE_SCHEMA.md`

### Phase 4

* [x] Implement application services for current `clubs` use cases
* [x] Implement command/query/result models only where required by those services
* [x] Keep the services transport-agnostic so future sync and recommendation work can depend on them

### Phase 5

* [x] Add unit tests for `clubs` domain and application behavior where applicable
* [x] Add persistence and adapter tests for `clubs` JPA mapping and repository behavior
* [x] Add integration tests that verify `clubs` persistence and retrieval flows against the documented schema
* [x] Run `./gradlew test`
* [x] Run `./gradlew check`

### Phase 6

* [x] Execute runtime verification against the running application and PostgreSQL for `clubs`
* [x] Record verification results
* [x] Update Plan status, task progress, and validation results

---

## Dependencies

Related Plans (Optional):

* PLAN-0011

Related Decisions (Optional):

* DECISION-0004
* DECISION-0005

---

## Progress Log

### 2026-06-11

* Plan created
* Status set to Proposed

### 2026-06-11

* Plan approved by user
* Status set to Approved

### 2026-06-11

* Implementation started
* Status set to In Progress

### 2026-06-11

* Implemented the initial `club` domain baseline for `clubs` only
* Added `Club`, `ClubRepository`, `ClubJpaEntity`, `ClubJpaRepository`, `ClubPersistenceAdapter`, `SaveClubService`, and `GetClubByCodeService`
* Added application command/query/result models for `clubs`
* Added unit, adapter, integration, and runtime verification tests for `clubs`
* Executed `./gradlew test`
* Verification result: `BUILD SUCCESSFUL`
* Executed `./gradlew check`
* Verification result: `BUILD SUCCESSFUL`
* Executed runtime verification through `ClubRuntimeVerificationTest` using the running Spring application context and PostgreSQL
* Runtime verification result: persisted and retrieved a `clubs` row successfully through `SaveClubService` and `GetClubByCodeService`
* Status set to Completed

---

## Validation

Describe how this Plan should be verified.

* Lint/static checks: project Gradle compile and verification checks
* Unit tests: `clubs` domain model behavior and application service behavior where applicable
* Integration tests: JPA entity mapping, adapter behavior, and persistence round-trip checks for `clubs`
* Manual verification:
  * inspect the new `club` package structure
  * confirm table ownership aligns with `PACKAGE_STRUCTURE.md`
  * confirm `clubs` persistence mapping aligns with `DATABASE_SCHEMA.md`
  * confirm deferred synchronization-only concerns are not implemented implicitly
* Validation result:
  * `clubs`-only scope preserved
  * no `club_tags` or `club_dna_scores` implementation was added
  * command/query/result, domain, repository, entity, and adapter layers were added for `clubs`
  * `./gradlew test` passed
  * `./gradlew check` passed

---

## Runtime Verification

Required because this Plan introduces executable persistence behavior and application wiring.

Verification steps:

1. Start the application against the configured PostgreSQL database.
2. Verify that the application boots successfully with the new Club Domain entity and repository registered.
3. Execute at least one runtime `clubs` persistence and retrieval smoke flow using the running application context.
4. Confirm that persisted `clubs` rows can be read back correctly through the implemented Club Domain path.

Success criteria:

* application startup succeeds with no JPA mapping failure
* `clubs` persistence and retrieval smoke flow succeeds
* persisted `clubs` rows are readable through the implemented Club Domain

Verification result:

* Passed on 2026-06-11
* Executed `./gradlew test --tests com.kopite.fd.club.application.service.ClubRuntimeVerificationTest`
* Running Spring application context connected to PostgreSQL
* Persisted a unique `clubs.code`
* Retrieved the same row through `GetClubByCodeService`

---

## Completion Criteria

This Plan is considered complete when:

* [x] All tasks are completed
* [x] `club` package ownership is implemented for `clubs`
* [x] Domain model, repository interface, JPA entity, repository, and adapter exist for `clubs`
* [x] Application services required for current `clubs` use cases exist
* [x] `clubs` persistence mapping aligns with `DATABASE_SCHEMA.md`
* [x] Scope requirements are satisfied
* [x] Lint or static checks pass
* [x] Related tests pass
* [x] Runtime verification passes
* [x] Validation results are recorded
* [x] Plan status updated in `docs/plans/README.md`
