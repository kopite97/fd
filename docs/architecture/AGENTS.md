# Architecture References

Review the following documents before implementing backend features.

## Available Documents

- BACKEND_ARCHITECTURE.md
- PACKAGE_STRUCTURE.md
- DATABASE_SCHEMA.md
- ENTITY_CONVENTIONS.md
- API_CONVENTIONS.md
- CODING_CONVENTIONS.md

---

## Architecture Source of Truth

The documents in this directory define the backend architecture source of truth.

Implementation must follow these documents when applicable.

Do not introduce new architectural patterns, package structures, entity conventions, API conventions, or coding conventions that conflict with these documents.

---

## Document Priority

When multiple documents apply:

1. AGENTS.md
2. BACKEND_ARCHITECTURE.md
3. DATABASE_SCHEMA.md
4. PACKAGE_STRUCTURE.md
5. ENTITY_CONVENTIONS.md
6. API_CONVENTIONS.md
7. CODING_CONVENTIONS.md

If a conflict is found:

1. Follow the higher-priority document.
2. Follow the most specific applicable document.
3. Ask for clarification rather than making assumptions.

---

## Common Reference Sets

### New Entity

Review:

* DATABASE_SCHEMA.md
* ENTITY_CONVENTIONS.md
* CODING_CONVENTIONS.md

### New Repository

Review:

* DATABASE_SCHEMA.md
* ENTITY_CONVENTIONS.md
* CODING_CONVENTIONS.md

### New Service

Review:

* BACKEND_ARCHITECTURE.md
* CODING_CONVENTIONS.md

### New API

Review:

* API_CONVENTIONS.md
* CODING_CONVENTIONS.md

### New Domain

Review:

* BACKEND_ARCHITECTURE.md
* PACKAGE_STRUCTURE.md

### New Persistence Logic

Review:

* DATABASE_SCHEMA.md
* ENTITY_CONVENTIONS.md
* BACKEND_ARCHITECTURE.md

### Data Import Feature

Review:

* BACKEND_ARCHITECTURE.md
* PACKAGE_STRUCTURE.md
* DATABASE_SCHEMA.md
* CODING_CONVENTIONS.md

### Refactoring Existing Code

Review:

* CODING_CONVENTIONS.md
* PACKAGE_STRUCTURE.md

---

## BACKEND_ARCHITECTURE.md

Defines backend architecture, layer responsibilities, dependency direction, and transaction boundaries.

Review when:

* creating new backend features
* designing new domains
* defining application services
* defining domain services
* introducing infrastructure integrations
* modifying dependency relationships between layers
* implementing transaction boundaries
* reviewing architectural consistency

---

## PACKAGE_STRUCTURE.md

Defines package ownership and package organization rules.

Review when:

* creating new packages
* moving classes between packages
* creating new domains
* deciding where a class belongs
* modifying package structure

---

## DATABASE_SCHEMA.md

Defines database tables, columns, constraints, and persistence structure.

Review when:

* creating or modifying JPA entities
* creating repositories
* implementing persistence logic
* adding database columns
* aligning entities with the documented schema

---

## ENTITY_CONVENTIONS.md

Defines JPA entity implementation standards.

Review when:

* creating new entities
* modifying existing entities
* implementing entity relationships
* defining identifiers
* implementing audit fields
* configuring JPA mappings

---

## API_CONVENTIONS.md

Defines REST API design standards.

Review when:

* creating controllers
* modifying endpoints
* creating request DTOs
* creating response DTOs
* defining API contracts
* documenting APIs

---

## CODING_CONVENTIONS.md

Defines Java coding conventions and Lombok usage rules.

Review when:

* writing new Java classes
* modifying existing classes
* implementing DTOs
* implementing services
* implementing controllers
* implementing repositories
* applying Lombok annotations

---

If multiple documents apply, all applicable documents must be reviewed before implementation.
