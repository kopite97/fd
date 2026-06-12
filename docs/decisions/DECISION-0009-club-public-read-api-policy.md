# DECISION-0009: Club Public Read API Policy

## Metadata

| Field         | Value                           |
|---------------|---------------------------------|
| Decision ID   | DECISION-0009                   |
| Title         | Club Public Read API Policy     |
| Status        | Accepted                        |
| Superseded By |                                 |
| Created At    | 2026-06-12                      |
| Updated At    | 2026-06-12                      |

---

## Context

The Club Domain currently has implemented data models and read APIs for:

* `clubs`
* `club_tags`
* `club_dna_scores`

However, there is no public read API for the `clubs` table itself.

Recommendation result screens, Team DNA pages, and club detail pages need stable access to basic club information such as name, code, league, colors, status, and beginner accessibility.

The public API contract must define:

* whether both list and detail lookups are supported
* how inactive and logically deleted clubs are handled
* whether pagination is required
* what default sorting is used
* whether summary and detail DTOs are separated

These choices affect public API behavior, client contracts, and future recommendation flows.

---

## Decision

Implement a public, read-only Club API for active, non-deleted clubs.

### 1. API Scope

The initial public Club API should support both:

* club list lookup
* single club detail lookup by `clubId`

Endpoints:

```text
GET /api/clubs
GET /api/clubs/{clubId}
```

Lookup by `code` is not included in the initial public API.

If a future client requires code-based lookup, it should be added through a separate approved Plan or explicit Plan update.

### 2. Visibility Policy

Public Club API reads must include only clubs where:

* `is_active = true`
* `is_deleted = false`

Rules:

* `is_deleted = true` clubs must never be exposed through public Club APIs.
* `is_active = false` clubs must not be exposed through public Club APIs in the initial implementation.
* Administrative or internal APIs may expose inactive clubs in the future, but they must be implemented separately under an approved admin/internal scope.

This follows `DECISION-0005` for soft delete behavior and treats `is_active` as a public visibility flag for club master data.

### 3. Pagination Policy

`GET /api/clubs` should be pageable from the initial implementation.

Default behavior:

* `page = 0`
* `size = 20`

Rationale:

* MVP data currently targets EPL clubs, but the product is expected to support additional leagues later.
* `API_CONVENTIONS.md` recommends pagination for resources where volume may grow.
* Starting with pagination avoids changing the public list response contract later.

### 4. Sorting Policy

The default list ordering should be deterministic:

1. `id` ascending

The current `clubs` schema does not include a `display_order` or source-order column.

Football DNA Data source order must not be inferred unless a later schema or synchronization Decision defines where that order is stored.

If user-facing source order becomes required, add an explicit column such as `display_order` through a separate Decision and Plan.

### 5. DTO Policy

Use separate response DTOs for list and detail:

* `ClubSummaryResponse` for list items
* `ClubDetailResponse` for detail lookup

The summary DTO should contain fields needed for cards and selection lists.

The detail DTO may include the full public club profile from the `clubs` table.

DTOs must not expose JPA entities, domain models, audit timestamps, or deleted state.

### 6. Not Found Policy

`GET /api/clubs/{clubId}` should return `404 Not Found` when:

* no club exists with the requested ID
* the club exists but `is_deleted = true`
* the club exists but `is_active = false`

Public clients should not be able to distinguish hidden, deleted, and non-existent club records.

### 7. Relationship To Child Data

This API exposes club base data only.

It does not embed:

* club tags
* club DNA scores
* recommendation explanations

Existing dedicated endpoints remain responsible for `club_tags` and `club_dna_scores`.

A composite Team DNA read model may be introduced later through a separate approved Plan if needed.

---

## Alternatives Considered

### Option A

Expose all clubs except logically deleted clubs, including inactive clubs.

Pros:

* useful for internal tooling
* simpler filtering logic

Cons:

* public clients may show clubs that should be hidden
* blurs the distinction between public and admin read models
* conflicts with `is_active` visibility intent documented in `DATABASE_SCHEMA.md`

### Option B

Expose only active, non-deleted clubs through the public API.

Pros:

* matches public visibility expectations
* preserves `is_deleted` and `is_active` as distinct lifecycle controls
* avoids leaking hidden clubs
* aligns with future admin API separation

Cons:

* inactive clubs require a separate admin/internal API to inspect
* tests must explicitly verify both filters

### Option C

Return an unpaged list because MVP has only EPL clubs.

Pros:

* smallest initial implementation
* simple response shape

Cons:

* creates a public contract that may need breaking changes when more leagues are added
* conflicts with API guidance to avoid unbounded collections when data may grow

### Option D

Use pageable list responses from the beginning.

Pros:

* future-proof public contract
* aligns with `API_CONVENTIONS.md`
* still supports EPL MVP with default `size = 20`

Cons:

* slightly more DTO and test code

---

## Consequences

Positive:

* public clients can retrieve club base data without using tag or DNA score endpoints
* inactive and deleted records are consistently hidden from public reads
* list responses remain stable as more clubs are added
* detail and list DTOs can evolve independently
* recommendation result and Team DNA pages get a clear base-data contract

Negative:

* admin visibility for inactive clubs remains unimplemented
* Football DNA Data source ordering is not represented until the schema explicitly supports it
* code-based lookup is deferred
* composite club profile responses remain out of scope

---

## Related Plans

* PLAN-0015

---

## Change History

### 2026-06-12

* Decision created
* Status set to Proposed

### 2026-06-12

* Decision approved by user
* Status set to Accepted
