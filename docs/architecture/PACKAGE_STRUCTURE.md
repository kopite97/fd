# Package Structure

## Root Package

Base package:

```text
src/main/java/com/kopite/fd
```

Top-level structure:

```text
com.kopite.fd
├── global
├── user
├── assessment
├── club
├── recommendation
├── ai
└── harness
```

---

## Domain Package Structure

Each domain may use the following structure when required:

```text
domain-name
├── controller
├── application
├── domain
├── infrastructure
└── dto
```

Not all packages are required for every domain.

Packages should only be created when they have a clear responsibility.

---

## Package Responsibilities

### controller

HTTP API entry points.

Examples:

* REST controllers
* Request mapping
* HTTP response handling

---

### application

Use-case orchestration.

Examples:

* Application services
* Transaction boundaries
* Coordination between domain objects and repositories

---

### domain

Business models and business rules.

Examples:

* Entities
* Value objects
* Domain services
* Repository interfaces

---

### infrastructure

Technical implementations.

Examples:

* JPA repository implementations
* External API clients
* Persistence adapters

---

### dto

Request and response models.

When complexity increases, DTOs may be organized by responsibility.

Example:

```text
dto
├── request
└── response
```
