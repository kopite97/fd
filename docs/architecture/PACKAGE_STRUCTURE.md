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
├── dna
├── club
├── recommendation
├── ai
├── harness
└── admin
```

---

## Top-Level Package Responsibilities

### global

Shared infrastructure and cross-cutting concerns used across multiple domains.

Examples:

* common configuration
* common exception handling
* common response models
* shared base entity
* shared utilities

Domain-specific business logic must not be placed in `global`.

---

### user

Registered user identity and user profile responsibility.

Owns:

* `users`

---

### assessment

Assessment execution, questions, answers, option scoring, and calculated user DNA scores.

Owns:

* `user_assessments`
* `questions`
* `question_options`
* `option_score_mappings`
* `assessment_answers`
* `assessment_dna_scores`

---

### dna

Shared DNA master data.

Owns:

* `dna_definitions`

Shared DNA master data belongs to `dna`, even when referenced by assessment, club, or recommendation features.

---

### club

Club master data and club-specific DNA metadata.

Owns:

* `clubs`
* `club_dna_scores`
* `club_tags`

---

### recommendation

Generated recommendation results and recommendation-specific AI adjustment history.

Owns:

* `assessment_recommendations`
* `assessment_ai_adjustments`

Recommendation-specific AI decisions and stored results belong to `recommendation`.

---

### ai

External AI integration logic.

Owns:

* external AI clients
* prompt execution
* model adapters

`ai` should not own persistence tables unless a future Decision says otherwise.

---

### harness

Development-time validation, automation, and project verification helpers.

Owns:

* validation helpers
* automation helpers
* project verification utilities

---

### admin

Administrator-only functionality.

Examples:

* Football DNA Data import
* operational tools
* management APIs

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

Do not create empty packages.

Feature-specific infrastructure may use alternative subpackages when the responsibility is clear and documented in this file.

---

## Database Table Ownership

Each database table should be implemented under the package that owns its domain responsibility.

```text
user
└── users

assessment
├── user_assessments
├── questions
├── question_options
├── option_score_mappings
├── assessment_answers
└── assessment_dna_scores

dna
└── dna_definitions

club
├── clubs
├── club_dna_scores
└── club_tags

recommendation
├── assessment_recommendations
└── assessment_ai_adjustments

ai
└── No database tables
```

Ownership rules:

* `user` owns registered user identity data.
* `assessment` owns assessment sessions, questions, answers, option scoring, and calculated user DNA scores.
* `dna` owns shared DNA master definitions.
* `club` owns club master data, club DNA scores, and club tags.
* `recommendation` owns generated recommendation results and AI adjustment history.
* `ai` owns AI integration logic only and should not own persistence tables unless a future Decision says otherwise.

---

## Global Package Structure

The `global` package contains cross-cutting infrastructure shared by multiple domains.

Recommended structure:

```text
global
├── config
├── exception
├── response
├── infrastructure
│   └── entity
│       └── BaseEntity
└── util
```

`BaseEntity` should provide common audit fields inherited by all JPA entities.

Common audit fields:

```text
created_at
updated_at
```

Domain packages should not define duplicate base entity classes.

Only create global subpackages when actual implementation exists.

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
