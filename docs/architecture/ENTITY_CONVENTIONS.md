# Entity Conventions

## Purpose

This document defines JPA entity implementation standards for the Football DNA backend.

All JPA entities must follow these conventions unless a documented exception exists.

---

## Entity Placement

JPA entities belong to:

```text
<domain>/infrastructure/entity
```

Example:

```text
club
└── infrastructure
    └── entity
        └── ClubJpaEntity
```

Do not place JPA entities in:

```text
controller
application
domain
dto
```

---

## Naming Conventions

Entity class names must end with:

```text
JpaEntity
```

Examples:

```java
UserJpaEntity
AssessmentJpaEntity
ClubJpaEntity
ClubDnaScoreJpaEntity
```

Database table names should follow the names defined in:

```text
DATABASE_SCHEMA.md
```

Example:

```java
@Entity
@Table(name = "clubs")
public class ClubJpaEntity
{
}
```

---

## Base Entity

All JPA entities must inherit:

```java
BaseEntity
```

Location:

```text
global.infrastructure.entity.BaseEntity
```

BaseEntity owns:

```java
createdAt
updatedAt
```

Individual entities must not redefine these fields.

Example:

```java
public class ClubJpaEntity extends BaseEntity
{
}
```

---

## Primary Keys

Every entity must define a primary key.

Example:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

Rules:

* Use wrapper types (`Long`, `Integer`, `Boolean`) instead of primitives.
* Avoid primitive types for identifiers.
* Do not expose identifier setters.
* Primary keys should be immutable after creation.

---

## Field Types

Entity field types must align with:

```text
DATABASE_SCHEMA.md
```

Standard mappings:

| Database Type | Java Type     |
| ------------- | ------------- |
| BIGINT        | Long          |
| INT           | Integer       |
| BOOLEAN       | Boolean       |
| VARCHAR       | String        |
| TEXT          | String        |
| DECIMAL(5,2)  | BigDecimal    |
| TIMESTAMP     | LocalDateTime |
| DATE          | LocalDate     |

Rules:

* Do not replace DECIMAL columns with `int`, `float`, or `double`.
* Use `BigDecimal` for fixed-point values.
* Keep entity types aligned with the documented schema.

---

## Lombok Usage

Use Lombok to reduce boilerplate.

Recommended:

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
```

Example:

```java
@Getter
@Entity
@Table(name = "clubs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubJpaEntity extends BaseEntity
{
}
```

---

## Prohibited Lombok Usage

Do not use:

```java
@Data
```

on JPA entities.

Do not use:

```java
@Setter
```

at class level.

Reasons:

* unwanted equals/hashCode generation
* unwanted toString generation
* JPA proxy issues
* unrestricted state mutation

---

## Builder Usage

`@Builder` is allowed on entities when it improves creation readability.

Example:

```java
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClubJpaEntity extends BaseEntity
{
}
```

Rules:

* Use Builder primarily for creation.
* Do not use Builder as a replacement for domain state transitions.
* Entity updates should occur through domain methods.

---

## Setter Usage

Avoid public setters whenever possible.

Prefer:

```java
public void updateName(String name)
{
    this.name = name;
}
```

instead of:

```java
public void setName(String name)
{
    this.name = name;
}
```

Entities should protect their own state.

---

## Constructors

Required creation values should be provided through:

* constructors
* static factory methods
* builders

Avoid creating entities through many setter calls.

Preferred:

```java
ClubJpaEntity.builder()
    .name(name)
    .code(code)
    .build();
```

---

## Relationships

Use explicit JPA relationships only when they provide clear value.

Examples:

```java
@ManyToOne
@OneToMany
@OneToOne
```

Rules:

* Prefer simple relationships.
* Avoid unnecessarily large object graphs.
* Avoid bidirectional relationships unless they provide clear value.
* Review relationship ownership carefully.

---

## Fetch Strategy

Default rule:

```java
FetchType.LAZY
```

Example:

```java
@ManyToOne(fetch = FetchType.LAZY)
```

Rules:

* Prefer LAZY loading.
* Use EAGER loading only with explicit justification.
* Prevent accidental N+1 query issues through query design rather than EAGER loading.

---

## Column Mapping

Always specify column metadata explicitly.

Example:

```java
@Column(
    name = "club_name",
    nullable = false,
    length = 100
)
private String name;
```

Rules:

* Always specify `name`.
* Specify `nullable` when known.
* Specify `length` for VARCHAR columns.
* Specify precision and scale for decimal columns.

Example:

```java
@Column(
    name = "score",
    precision = 5,
    scale = 2,
    nullable = false
)
private BigDecimal score;
```

---

## Nullable Rules

Nullability must align with:

```text
DATABASE_SCHEMA.md
```

Example:

```java
@Column(
    name = "name",
    nullable = false
)
private String name;
```

Required fields must be non-nullable.

---

## Enum Mapping

Enums must use:

```java
@Enumerated(EnumType.STRING)
```

Example:

```java
@Enumerated(EnumType.STRING)
@Column(name = "status")
private AssessmentStatus status;
```

Do not use:

```java
EnumType.ORDINAL
```

Reason:

* database values become unstable when enum ordering changes

---

## Auditing

Audit fields are managed automatically through:

```java
BaseEntity
```

Entities must not manually update:

```java
createdAt
updatedAt
```

Audit timestamps should be managed through JPA Auditing.

---

## Equality Rules

Avoid custom equals/hashCode implementations unless required.

If equality is needed:

* use identifier-based equality
* ensure compatibility with JPA proxies

Do not generate equals/hashCode automatically through Lombok `@Data`.

---

## Business Logic

Simple state transitions may exist inside entities.

Examples:

```java
complete()
activate()
deactivate()
updateNickname()
updateStatus()
```

Complex business workflows belong to:

```text
Application Service
Domain Service
```

not JPA entities.

---

## Entity Responsibilities

Entities are responsible for:

* persistence state
* domain state transitions
* invariant protection
* relationship ownership

Entities are not responsible for:

* HTTP processing
* request validation
* external API integration
* transaction management
* repository access
* application orchestration

---

## Schema Alignment

Before creating or modifying an entity, review:

* DATABASE_SCHEMA.md
* BACKEND_ARCHITECTURE.md
* CODING_CONVENTIONS.md

Entity structure must remain aligned with DATABASE_SCHEMA.md.

Schema changes should be reflected in entity mappings as part of the same implementation effort.

---

## Entity Review Checklist

Before completing entity work, verify:

* Entity is located in `infrastructure/entity`
* Entity extends `BaseEntity`
* Table name matches `DATABASE_SCHEMA.md`
* Column names match `DATABASE_SCHEMA.md`
* Field types match `DATABASE_SCHEMA.md`
* `@Data` is not used
* Class-level `@Setter` is not used
* Relationships use `FetchType.LAZY` unless justified
* Enums use `EnumType.STRING`
* Audit fields are not duplicated
* Business logic remains appropriate for the entity layer
