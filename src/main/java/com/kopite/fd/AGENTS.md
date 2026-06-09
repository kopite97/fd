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

Dependencies should flow inward:

```text
Controller → Application → Domain
Infrastructure → Domain
```

Infrastructure is responsible for persistence and external integrations.

---

## Dependency Rules

* Domain must not depend on Controller.
* Domain must not depend on Infrastructure.
* Application may depend on Domain.
* Infrastructure may depend on Domain.

---

## Package Structure

Before creating or modifying backend packages, review `docs/architecture/PACKAGE_STRUCTURE.md`.

Each domain should follow a consistent structure.

Example:

```text
recommendation
├── controller
├── application
├── domain
├── infrastructure
└── dto
```

* Create new packages only when there is a clear responsibility.
* Do not create empty packages without implementation.
* Do not create new layers or subpackages unless they have a clear responsibility.
* Prefer domain-oriented organization over technical grouping.

Infrastructure contains persistence implementations and external integrations.

Repository interfaces belong to the Domain layer.

Repository implementations belong to the Infrastructure layer.

Example:

```text
recommendation
├── domain
│   └── RecommendationRepository
│
├── infrastructure
│   └── JpaRecommendationRepository
```

DTOs should be organized by responsibility when complexity increases.

Example:

```text
dto
├── request
└── response
```

---

## Transaction Rules

* Define transactions in the Application(Service) layer.
* Do not manage transactions in Controllers.
* Avoid transactions in Domain objects.
* Use read-only transactions for query operations whenever appropriate.
* Keep transaction scope as small as possible.

---

## API Design Rules

* Follow RESTful conventions.
* Use DTOs for request and response models.
* Do not expose entities directly through APIs.
* Validate external input before business processing.
* Return consistent response structures.
* Keep controllers thin and focused on HTTP concerns.

If API behavior is unclear, ask for clarification rather than making assumptions.
