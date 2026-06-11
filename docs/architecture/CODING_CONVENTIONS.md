# Coding Conventions

## Purpose

This document defines Java coding conventions used throughout the Football DNA backend.

The goal is to improve consistency, readability, and maintainability.

---

## General Principles

Code should prioritize:

* readability
* consistency
* explicitness
* maintainability

Prefer simple solutions over clever solutions.

Avoid unnecessary abstraction.

---

## Naming Conventions

### Classes

Use:

```java
ClubJpaEntity
RecommendationService
ImportFootballDnaDataResult
```

Rules:

* Use PascalCase.
* Class names should clearly describe responsibility.

---

### Methods

Use:

```java
findById()
createAssessment()
calculateScore()
importData()
```

Rules:

* Use camelCase.
* Method names should start with a verb.

---

### Variables

Use:

```java
clubId
assessmentId
recommendationScore
```

Rules:

* Use camelCase.
* Use meaningful names.
* Avoid abbreviations unless widely understood.

Avoid:

```java
id
tmp
data
obj
```

when a more specific name is possible.

---

### Constants

Use:

```java
private static final int MAX_RECOMMENDATION_COUNT = 5;
```

Rules:

* Use UPPER_SNAKE_CASE.

---

## Lombok

Use Lombok to reduce boilerplate code when it improves readability.

Prefer Lombok for:

* DTOs
* JPA entities
* configuration classes
* Spring-managed components

Do not add Lombok annotations automatically when they reduce clarity.

---

## JPA Entity

Allowed:

* `@Getter`
* `@Builder`
* `@NoArgsConstructor(access = AccessLevel.PROTECTED)`
* `@AllArgsConstructor(access = AccessLevel.PRIVATE)` (when Builder is used)

Avoid:

* `@Setter`
* `@Data`

Entity state changes should be expressed through explicit methods.

Example:

```java
@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserJpaEntity extends BaseEntity
{
}
```

---

## DTO

Allowed:

* `@Getter`
* `@Builder`
* `@NoArgsConstructor`
* `@AllArgsConstructor`

DTOs may use Lombok when it improves readability.

Example:

```java
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubResponse
{
    private Long clubId;
    private String clubName;
}
```

---

## Spring Beans

For Spring-managed classes such as:

* `@Service`
* `@Component`
* `@Controller`
* `@RestController`

Use constructor injection with:

* `private final` fields
* `@RequiredArgsConstructor`

Example:

```java
@Service
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;
}
```

Do not use field injection.

Avoid:

```java
@Autowired
private UserRepository userRepository;
```

---

## Constructor Rules

Use `@RequiredArgsConstructor` when no custom constructor logic is needed.

Write constructors manually only when construction requires:

* validation
* invariant checks
* custom initialization

---

## Collection Fields

Initialize collection fields immediately.

Prefer:

```java
private final List<String> tags = new ArrayList<>();
```

Avoid:

```java
private List<String> tags;
```

---

## Optional Usage

Use:

```java
Optional<T>
```

for repository query results that may not exist.

Example:

```java
Optional<ClubJpaEntity> findById(Long id);
```

Do not use Optional:

* as entity fields
* as DTO fields
* as method parameters

---

## Date and Time

Use:

* `LocalDate`
* `LocalDateTime`

Avoid:

* `Date`
* `Calendar`
* `Timestamp`

---

## BigDecimal

Use `BigDecimal` for:

* scores
* percentages
* calculated recommendation values
* schema-defined decimal columns

Avoid:

```java
double
float
```

for business calculations.

---

## Null Handling

Prefer explicit null handling.

Use:

* validation
* Optional
* domain constraints

Avoid deep null chains.

Example:

```java
if (club == null)
{
    throw new ClubNotFoundException();
}
```

---

## Stream Usage

Streams may be used when they improve readability.

Prefer:

```java
clubs.stream()
     .map(...)
     .toList();
```

Avoid overly complex stream chains.

If a stream becomes difficult to read, use a loop.

---

## Method Size

Methods should generally perform one responsibility.

Consider extraction when:

* a method becomes difficult to understand
* multiple responsibilities appear
* nested conditions become excessive

---

## Logging

Use:

```java
@Slf4j
```

for logging.

Log:

* important business events
* integration failures
* unexpected errors

Do not log:

* passwords
* tokens
* secrets
* sensitive user data

---

## Exception Handling

Throw meaningful exceptions.

Prefer:

```java
ClubNotFoundException
AssessmentNotFoundException
```

Avoid:

```java
throw new RuntimeException(...)
```

unless no better exception exists.

---

## Comments

Prefer self-explanatory code.

Avoid comments that repeat the code.

Bad:

```java
// increment count
count++;
```

Good:

```java
// Recommendation scores must remain stable across reruns
```

Comments should explain:

* business rules
* constraints
* architectural decisions

---

## Formatting

Rules:

* Use 4 spaces for indentation.
* One public class per file.
* Keep imports organized automatically through IDE formatting.
* Remove unused imports.

---

## Review Checklist

Before completing implementation, verify:

* Naming follows conventions
* Constructor injection is used
* Field injection is not used
* Entity rules are respected
* DTO rules are respected
* Optional usage is appropriate
* Date/time types follow standards
* Decimal values use BigDecimal where required
* Logging is appropriate
* Exceptions are meaningful
* Code remains readable
