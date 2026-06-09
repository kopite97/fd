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
├── controller
├── dto
│   ├── request
│   └── response
├── application
│   ├── command
│   ├── query
│   ├── service
│   └── result
├── domain
│   ├── model
│   ├── repository
│   ├── type
│   └── service
└── infrastructure
    ├── entity
    ├── repository
    ├── adapter
    └── mapper
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

Subpackages:

- `command`: input objects for state-changing use cases
- `query`: input objects for read-only use cases
- `service`: application services and transaction boundaries
- `result`: output objects returned by application services

---

### domain

Business models and business rules.

Subpackages:

- `model`: aggregates, entities, and value objects
- `repository`: domain repository interfaces
- `type`: enums and domain-specific types
- `service`: domain services when business logic does not naturally belong to one model

---

### infrastructure

Technical implementations.

Subpackages:

- `entity`: JPA entities and persistence-specific models
- `repository`: Spring Data JPA repositories
- `adapter`: implementations of domain repository interfaces
- `mapper`: conversions between domain models and infrastructure entities

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
