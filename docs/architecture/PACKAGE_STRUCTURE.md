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
├── harness
└── admin
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

## Admin Package Structure

The `admin` package groups administrator-only features.

Admin-specific use cases should be organized by feature under `admin` instead of placing all admin controllers, services, DTOs, and infrastructure classes directly under `admin`.

Example:

```text
admin
└── dataimport
    ├── controller
    ├── dto
    │   ├── request
    │   └── response
    ├── application
    │   ├── command
    │   ├── service
    │   └── result
    ├── domain
    │   ├── model
    │   └── type
    └── infrastructure
        ├── datasource
        ├── parser
        ├── converter
        └── artifact
```

Use `admin.dataimport` for administrator-only data import workflows such as Football DNA Data import.

Do not place unrelated admin features in `admin.dataimport`.

When new administrator-only features are added, create a separate feature package under `admin`.

Example:

```text
admin
├── dataimport
├── dashboard
└── operation
```

Only create these packages when actual implementation exists.

---

## Data Import Infrastructure Structure

The `admin.dataimport.infrastructure` package may be divided by integration responsibility.

```text
infrastructure
├── datasource
├── parser
├── converter
└── artifact
```

Responsibilities:

* `datasource`: retrieves raw data from configured external or internal sources
* `parser`: interprets source formats such as CSV or JSON
* `converter`: converts parsed data into application-level import models
* `artifact`: writes import evidence such as Markdown or raw snapshot files

Application services must depend on abstractions and should not directly depend on source-format-specific implementations.

Examples:

```text
datasource
└── PublishedCsvFootballDnaDataSource

parser
└── CsvFootballDnaDataParser

converter
└── FootballDnaImportDataConverter

artifact
└── MarkdownImportArtifactWriter
```

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

* `command`: input objects for state-changing use cases
* `query`: input objects for read-only use cases
* `service`: application services and transaction boundaries
* `result`: output objects returned by application services

---

### domain

Business models and business rules.

Subpackages:

* `model`: aggregates, entities, and value objects
* `repository`: domain repository interfaces
* `type`: enums and domain-specific types
* `service`: domain services when business logic does not naturally belong to one model

---

### infrastructure

Technical implementations.

Subpackages:

* `entity`: JPA entities and persistence-specific models
* `repository`: Spring Data JPA repositories
* `adapter`: implementations of domain repository interfaces
* `mapper`: conversions between domain models and infrastructure entities

Feature-specific infrastructure may use additional subpackages when the responsibility is clear.

Examples:

* `datasource`
* `parser`
* `converter`
* `artifact`

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
