# Backend Architecture

## Layer Architecture

The backend follows the structure below:

```text
Controller
    ↓
Application(Service)
    ↓
Domain

Infrastructure
 ├─ Database
 └─ External APIs
```

Responsibilities:

* Controller: request and response handling
* Application(Service): use-case orchestration, transaction management, and coordination between domain objects
* Domain: business rules, domain models, and domain-specific validation
* Infrastructure: persistence and external integrations

Business logic should remain in the Application and Domain layers.

Domain should remain framework-independent whenever possible.

Application coordinates business flows but should not contain persistence implementation details.

Infrastructure is responsible for persistence and external integrations, including:

* Database integrations
* Google Sheets integrations
* CSV import/export
* External APIs
* Other external systems

Dependencies should flow inward:

```text
Controller → Application → Domain
Infrastructure → Domain
```

---

## Dependency Rules

* Domain must not depend on Controller.

* Domain must not depend on Infrastructure.

* Application may depend on Domain.

* Infrastructure may depend on Domain.

* Infrastructure must not depend on Application.

* Controller must not depend on Infrastructure directly.

Repository ownership:

* Repository interfaces belong to the Domain layer.
* Repository implementations belong to the Infrastructure layer.

Example:

```text
club
├── domain
│   └── repository
│       └── ClubRepository
└── infrastructure
    └── repository
        └── JpaClubRepository
```

---

## Transaction Rules

* Define transactions in the Application(Service) layer.
* Do not manage transactions in Controllers.
* Avoid transactions in Domain objects.
* Use read-only transactions for query operations whenever appropriate.
* Keep transaction scope as small as possible.

---

## Application Layer Rules

The Application layer is responsible for orchestrating use cases.

Responsibilities include:

* coordinating domain objects
* managing transactions
* invoking repository interfaces
* coordinating external workflows through abstractions

The Application layer should not:

* contain persistence implementation details
* directly use infrastructure implementations
* contain HTTP-specific behavior

Read and write use cases may be separated through query and command models when appropriate.

Example:

```text
application
├── command
├── query
├── service
└── result
```

---

## Domain Layer Rules

The Domain layer contains business concepts and business rules.

Responsibilities include:

* domain models
* value objects
* aggregates
* domain services
* domain-specific validation
* repository interfaces

The Domain layer should:

* remain framework-independent whenever possible
* avoid Spring-specific annotations and infrastructure concerns
* avoid HTTP concerns
* avoid persistence implementation details

---

## Infrastructure Layer Rules

The Infrastructure layer contains technical implementations.

Responsibilities include:

* JPA repositories
* external API integrations
* Google Sheets integrations
* CSV parsers
* data import adapters
* persistence implementations
* artifact generation
* infrastructure-specific mapping

Infrastructure implementations may depend on Domain abstractions but must not depend on Application services.

---

## Controller Layer Rules

Controllers are responsible for HTTP concerns only.

Responsibilities include:

* request handling
* response generation
* request validation
* HTTP status management

Controllers should:

* remain thin
* delegate business processing to Application services
* avoid direct database access
* avoid transaction management
* avoid Infrastructure dependencies

---

## Data Import Architecture

Football DNA data import follows the standard backend architecture.

Example:

```text
Controller
    ↓
Application
    ↓
Domain

Infrastructure
 ├─ Data Source
 ├─ Parser
 ├─ Converter
 └─ Artifact Writer
```

Rules:

* Data acquisition belongs to Infrastructure.
* Parsing belongs to Infrastructure.
* Source-specific schema handling belongs to Infrastructure.
* Application orchestrates import workflows through abstractions.
* Application must not depend on CSV-specific or JSON-specific implementations.
* Source implementations should be replaceable without modifying Application orchestration.

---

## Architectural Consistency

When introducing new features:

* Follow the defined dependency direction.
* Keep business logic in Application and Domain layers.
* Place external integrations in Infrastructure.
* Keep Controllers focused on HTTP concerns.
* Prefer abstractions over direct infrastructure coupling.
* Follow `PACKAGE_STRUCTURE.md` for package ownership.
* Follow `DATABASE_SCHEMA.md` when implementing persistence models.
* Follow `ENTITY_CONVENTIONS.md` when implementing JPA entities.
* Follow `API_CONVENTIONS.md` when implementing APIs.
* Follow `CODING_CONVENTIONS.md` when implementing code.
