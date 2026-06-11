# API Conventions

## Purpose

This document defines REST API implementation standards for the Football DNA backend.

All controllers, request DTOs, response DTOs, and API contracts should follow these conventions.

---

## REST Principles

Follow RESTful conventions whenever practical.

Examples:

```text
GET    /api/clubs
GET    /api/clubs/{clubId}

POST   /api/assessments
POST   /api/assessments/{assessmentId}/answers

GET    /api/recommendations/{assessmentId}
```

Prefer resource-oriented endpoints.

Avoid action-oriented endpoints when a resource-oriented design is appropriate.

Avoid:

```text
POST /api/createAssessment
POST /api/getRecommendations
```

Prefer:

```text
POST /api/assessments
GET  /api/recommendations/{assessmentId}
```

---

## URL Conventions

Use:

```text
kebab-case
```

for URL paths.

Examples:

```text
/assessment-results
/club-dna-scores
/public-results
```

Path variables should be nouns.

Example:

```text
/api/clubs/{clubId}
```

---

## HTTP Method Rules

Use:

| Method | Purpose        |
| ------ | -------------- |
| GET    | Read           |
| POST   | Create         |
| PUT    | Replace        |
| PATCH  | Partial Update |
| DELETE | Remove         |

Examples:

```text
GET    /api/clubs
POST   /api/assessments
PATCH  /api/users/{userId}
DELETE /api/admin/import-history/{id}
```

---

## Controller Responsibilities

Controllers are responsible only for:

* HTTP request handling
* request validation
* response generation
* HTTP status handling

Controllers should:

* remain thin
* delegate business logic to Application Services
* avoid transaction management
* avoid persistence logic
* avoid external integration logic

Example:

```java
return recommendationService.recommend(request);
```

Good.

```java
entityManager.persist(...)
```

Bad.

---

## DTO Rules

Always use DTOs.

Do not expose:

```java
JpaEntity
Domain Model
```

directly through APIs.

Request DTOs:

```text
dto/request
```

Response DTOs:

```text
dto/response
```

Example:

```text
recommendation
└── dto
    ├── request
    └── response
```

---

## Validation Rules

Validate external input before business processing.

Use Bean Validation annotations.

Examples:

```java
@NotNull
@NotBlank
@Size
@Email
```

Controller parameters should use:

```java
@Valid
```

Example:

```java
@PostMapping
public Response create(
        @Valid @RequestBody CreateRequest request
)
```

---

## Response Rules

Return consistent response structures.

Successful responses may return:

```java
ResponseEntity<T>
```

or

```java
T
```

depending on project conventions.

The same style should be used consistently across the project.

---

## HTTP Status Rules

Use appropriate status codes.

Examples:

| Status                    | Usage              |
| ------------------------- | ------------------ |
| 200 OK                    | Successful request |
| 201 Created               | Resource created   |
| 204 No Content            | Successful delete  |
| 400 Bad Request           | Invalid request    |
| 404 Not Found             | Resource not found |
| 409 Conflict              | Business conflict  |
| 500 Internal Server Error | Unexpected failure |

---

## Error Handling

Use centralized exception handling.

Preferred:

```java
@RestControllerAdvice
```

Controllers should not manually build error responses repeatedly.

Business exceptions should be translated into consistent API responses.

---

## Pagination

For pageable resources, use pagination.

Examples:

```text
GET /api/clubs?page=0&size=20
```

Avoid returning unbounded collections when data volume may grow.

---

## API Versioning

If versioning becomes necessary, use:

```text
/api/v1
```

Example:

```text
/api/v1/clubs
/api/v1/recommendations
```

Do not introduce API versioning until it is required.

---

## Swagger Documentation

Swagger annotations should be added for public APIs.

Examples:

```java
@Operation
@Parameter
@Schema
```

API documentation should remain synchronized with implementation.

---

## Admin APIs

Administrative APIs belong under:

```text
/api/admin
```

Examples:

```text
/api/admin/data-import
/api/admin/import-history
```

Administrative endpoints should not be mixed with public APIs.

---

## API Review Checklist

Before completing API work, verify:

* RESTful endpoint design is used
* Controller remains thin
* DTOs are used
* Entities are not exposed
* Validation is applied
* HTTP status codes are appropriate
* Swagger documentation is updated
* Business logic remains outside the Controller
* API follows package conventions
