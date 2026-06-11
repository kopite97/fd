# Database Schema

## Overview

This document describes the database tables used by Football DNA.

All tables inherit common audit columns through a shared base entity.

---

## Common Audit Columns

The following columns are inherited by all tables through the shared `BaseEntity`.

| column_name | data_type | constraints | nullable | default_value     | description           | note                                |
| ----------- | --------- | ----------- | -------- | ----------------- | --------------------- | ----------------------------------- |
| created_at  | TIMESTAMP |             | NO       | CURRENT_TIMESTAMP | Creation timestamp    |                                     |
| updated_at  | TIMESTAMP |             | NO       | CURRENT_TIMESTAMP | Last update timestamp |                                     |
| is_deleted  | BOOLEAN   |             | NO       | FALSE             | Soft delete flag      | `false` = active, `true` = deleted |

### Soft Delete Rules

* `is_deleted = false` indicates an active record.
* `is_deleted = true` indicates a logically deleted record.
* Logical deletion should be preferred over physical deletion for business data unless an approved Decision explicitly allows physical deletion.
* The existence of `is_deleted` does not require every table to actively use soft delete semantics.
* Snapshot, history, and versioned tables should preserve committed records and should not rely on soft delete as their primary lifecycle mechanism.

---

## Version Identifier Policy

The project uses string-based version identifiers.

| Version Field | Example |
|---------------|----------|
| question_version | q-v1 |
| algorithm_version | alg-v1 |
| club_data_version | club-v1 |
| club_dna_scores.data_version | club-v1 |
| explanation_version | exp-v1 |
| prompt_version | prompt-v1 |

Each version field has its own namespace and prefix.

Version identifiers must be compared by exact equality only.
They must not be sorted or compared as numeric versions.

---

# users

## Purpose

Stores registered users.

## Columns

| column_name      | data_type    | constraints | nullable | default_value     | description               | note                    |
| ---------------- | ------------ | ----------- | -------- | ----------------- | ------------------------- | ----------------------- |
| id               | BIGINT       | PK          | NO       |                   | User identifier           | Internal user ID        |
| email            | VARCHAR(255) | UNIQUE      | YES      |                   | User email address        | OAuth login email       |
| nickname         | VARCHAR(50)  |             | YES      |                   | User nickname             | Nullable in MVP         |
| provider         | VARCHAR(20)  |             | YES      |                   | OAuth provider            | `GOOGLE`, `KAKAO`, etc. |
| provider_user_id | VARCHAR(255) | UNIQUE      | YES      |                   | Provider-specific user ID | Unique per provider     |
| created_at       | TIMESTAMP    |             | NO       | CURRENT_TIMESTAMP | Creation timestamp        |                         |
| updated_at       | TIMESTAMP    |             | NO       | CURRENT_TIMESTAMP | Last update timestamp     |                         |
| is_deleted       | BOOLEAN      |             | NO       | FALSE             | Soft delete flag          |                         |

---

# user_assessments

## Purpose

Represents a single Football DNA assessment session.

## Columns

| column_name       | data_type    | constraints    | nullable | default_value     | description                      | note                                            |
| ----------------- | ------------ | -------------- | -------- | ----------------- | -------------------------------- | ----------------------------------------------- |
| id                | BIGINT       | PK             | NO       |                   | Assessment identifier            | Root ID for assessment result records           |
| user_id           | BIGINT       | FK -> users.id | YES      |                   | Associated user ID               | Nullable for anonymous assessments              |
| anonymous_id      | VARCHAR(100) | INDEX          | YES      |                   | Anonymous identifier             | Cookie or local-storage based identity          |
| public_result_key | VARCHAR(100) | UNIQUE         | YES      |                   | Public share key                 | Prevents direct exposure of `assessment_id`     |
| status            | VARCHAR(20)  |                | NO       | IN_PROGRESS       | Assessment status                | `IN_PROGRESS`, `COMPLETED`, `ABANDONED`         |
| question_version  | VARCHAR(20)  |                | NO       |                   | Question set version             | Frozen when the assessment starts               |
| algorithm_version | VARCHAR(20)  |                | YES      |                   | Recommendation algorithm version | Nullable until assessment completion freeze     |
| club_data_version | VARCHAR(20)  |                | YES      |                   | Club data version                | Nullable until recommendation generation freeze |
| started_at        | TIMESTAMP    |                | NO       | CURRENT_TIMESTAMP | Assessment start time            |                                                 |
| completed_at      | TIMESTAMP    |                | YES      |                   | Assessment completion time       | Null until the assessment is completed          |
| created_at        | TIMESTAMP    |                | NO       | CURRENT_TIMESTAMP | Creation timestamp               |                                                 |
| updated_at        | TIMESTAMP    |                | NO       | CURRENT_TIMESTAMP | Last update timestamp            |                                                 |
| is_deleted        | BOOLEAN      |                | NO       | FALSE             | Soft delete flag                 |                                                 |

---

# dna_definitions

## Purpose

Master table for all DNA attributes.

## Columns

| column_name   | data_type   | constraints | nullable | default_value     | description               | note                                  |
| ------------- | ----------- | ----------- | -------- | ----------------- | ------------------------- | ------------------------------------- |
| id            | BIGINT      | PK          | NO       |                   | DNA definition identifier | Referenced by related DNA tables      |
| dna_category  | VARCHAR(20) |             | NO       |                   | DNA category              | `EMOTIONAL`, `PLAYSTYLE`              |
| dna_key       | VARCHAR(50) |             | NO       |                   | Internal DNA key          | Example: `fan_culture`, `pressing`    |
| display_name  | VARCHAR(50) |             | NO       |                   | Display name              | User-facing label                     |
| description   | TEXT        |             | YES      |                   | DNA description           | Used in result and admin contexts     |
| display_order | INT         |             | NO       | 0                 | Display order             | Used in charts and admin ordering     |
| is_active     | BOOLEAN     |             | NO       | TRUE              | Active flag               | Inactive rows are excluded from scoring |
| created_at    | TIMESTAMP   |             | NO       | CURRENT_TIMESTAMP | Creation timestamp        |                                       |
| updated_at    | TIMESTAMP   |             | NO       | CURRENT_TIMESTAMP | Last update timestamp     |                                       |
| is_deleted    | BOOLEAN     |             | NO       | FALSE             | Soft delete flag          |                                       |

## Table Constraints

| constraint_type | columns               | name                            | description                                   |
| --------------- | --------------------- | ------------------------------- | --------------------------------------------- |
| UNIQUE          | dna_category, dna_key | uk_dna_definitions_category_key | Prevent duplicate `dna_key` within a category |

---

# questions

## Purpose

Stores assessment questions.

## Columns

| column_name               | data_type   | constraints              | nullable | default_value     | description               | note                                             |
| ------------------------- | ----------- | ------------------------ | -------- | ----------------- | ------------------------- | ------------------------------------------------ |
| id                        | BIGINT      | PK                       | NO       |                   | Question identifier       |                                                  |
| question_text             | TEXT        |                          | NO       |                   | Question text             |                                                  |
| question_type             | VARCHAR(20) |                          | NO       |                   | Question type             | `CHOICE`, `AI_FREE_TEXT`                         |
| primary_dna_definition_id | BIGINT      | FK -> dna_definitions.id | YES      |                   | Primary DNA definition ID | Multi-DNA impact is handled by score mappings    |
| display_order             | INT         |                          | NO       |                   | Display order             |                                                  |
| question_version          | VARCHAR(20) |                          | NO       |                   | Question version          | Must align with the `question_version` namespace |
| is_active                 | BOOLEAN     |                          | NO       | TRUE              | Active flag               |                                                  |
| created_at                | TIMESTAMP   |                          | NO       | CURRENT_TIMESTAMP | Creation timestamp        |                                                  |
| updated_at                | TIMESTAMP   |                          | NO       | CURRENT_TIMESTAMP | Last update timestamp     |                                                  |
| is_deleted                | BOOLEAN     |                          | NO       | FALSE             | Soft delete flag          |                                                  |

---

# question_options

## Purpose

Stores selectable options for questions.

## Columns

| column_name   | data_type | constraints        | nullable | default_value     | description           | note |
| ------------- | --------- | ------------------ | -------- | ----------------- | --------------------- | ---- |
| id            | BIGINT    | PK                 | NO       |                   | Option identifier     |      |
| question_id   | BIGINT    | FK -> questions.id | NO       |                   | Related question ID   |      |
| option_text   | TEXT      |                    | NO       |                   | Option text           |      |
| display_order | INT       |                    | NO       |                   | Display order         |      |
| is_active     | BOOLEAN   |                    | NO       | TRUE              | Active flag           |      |
| created_at    | TIMESTAMP |                    | NO       | CURRENT_TIMESTAMP | Creation timestamp    |      |
| updated_at    | TIMESTAMP |                    | NO       | CURRENT_TIMESTAMP | Last update timestamp |      |
| is_deleted    | BOOLEAN   |                    | NO       | FALSE             | Soft delete flag      |      |

---

# option_score_mappings

## Purpose

Maps selected options to DNA score changes.

## Columns

| column_name       | data_type    | constraints               | nullable | default_value     | description                  | note                                |
| ----------------- | ------------ | ------------------------- | -------- | ----------------- | ---------------------------- | ----------------------------------- |
| id                | BIGINT       | PK                        | NO       |                   | Mapping identifier           |                                     |
| option_id         | BIGINT       | FK -> question_options.id | NO       |                   | Related option ID            |                                     |
| dna_definition_id | BIGINT       | FK -> dna_definitions.id  | NO       |                   | Target DNA definition ID     |                                     |
| score_delta       | DECIMAL(5,2) |                           | NO       | 0                 | Score adjustment             | Applied when the option is selected |
| created_at        | TIMESTAMP    |                           | NO       | CURRENT_TIMESTAMP | Creation timestamp           |                                     |
| updated_at        | TIMESTAMP    |                           | NO       | CURRENT_TIMESTAMP | Last update timestamp        |                                     |
| is_deleted        | BOOLEAN      |                           | NO       | FALSE             | Soft delete flag             |                                     |

## Table Constraints

| constraint_type | columns                      | name                                | description                                           |
| --------------- | ---------------------------- | ----------------------------------- | ----------------------------------------------------- |
| UNIQUE          | option_id, dna_definition_id | uk_option_score_mappings_option_dna | Prevent duplicate DNA impact rows for the same option |

---

# assessment_answers

## Purpose

Stores user responses.

## Columns

| column_name         | data_type | constraints               | nullable | default_value     | description                   | note                                               |
| ------------------- | --------- | ------------------------- | -------- | ----------------- | ----------------------------- | -------------------------------------------------- |
| id                  | BIGINT    | PK                        | NO       |                   | Answer identifier             |                                                    |
| assessment_id       | BIGINT    | FK -> user_assessments.id | NO       |                   | Assessment identifier         | Aggregate FK                                       |
| question_id         | BIGINT    | FK -> questions.id        | NO       |                   | Question identifier           |                                                    |
| option_id           | BIGINT    | FK -> question_options.id | YES      |                   | Selected option ID            | Nullable for free-text questions                   |
| answer_text         | TEXT      |                           | YES      |                   | Free-text answer              | Used for AI free-text questions                    |
| score_snapshot_json | JSONB     |                           | YES      |                   | Score snapshot at answer time | Supports historical replay if scoring changes      |
| created_at          | TIMESTAMP |                           | NO       | CURRENT_TIMESTAMP | Creation timestamp            |                                                    |
| updated_at          | TIMESTAMP |                           | NO       | CURRENT_TIMESTAMP | Last update timestamp         |                                                    |
| is_deleted          | BOOLEAN   |                           | NO       | FALSE             | Soft delete flag              |                                                    |

---

# assessment_dna_scores

## Purpose

Stores calculated DNA scores for an assessment.

## Columns

| column_name       | data_type    | constraints               | nullable | default_value     | description               | note                   |
| ----------------- | ------------ | ------------------------- | -------- | ----------------- | ------------------------- | ---------------------- |
| id                | BIGINT       | PK                        | NO       |                   | DNA score identifier      |                        |
| assessment_id     | BIGINT       | FK -> user_assessments.id | NO       |                   | Assessment identifier     | Aggregate FK           |
| dna_definition_id | BIGINT       | FK -> dna_definitions.id  | NO       |                   | DNA definition identifier |                        |
| score             | DECIMAL(5,2) |                           | NO       | 0                 | Calculated DNA score      | Current range is 0-5   |
| created_at        | TIMESTAMP    |                           | NO       | CURRENT_TIMESTAMP | Creation timestamp        |                        |
| updated_at        | TIMESTAMP    |                           | NO       | CURRENT_TIMESTAMP | Last update timestamp     |                        |
| is_deleted        | BOOLEAN      |                           | NO       | FALSE             | Soft delete flag          |                        |

## Table Constraints

| constraint_type | columns                          | name                                    | description                                     |
| --------------- | -------------------------------- | --------------------------------------- | ----------------------------------------------- |
| UNIQUE          | assessment_id, dna_definition_id | uk_assessment_dna_scores_assessment_dna | Prevent duplicate DNA score rows per assessment |

---

# clubs

## Purpose

Stores football club information.

## Columns

| column_name            | data_type    | constraints | nullable | default_value     | description                      | note                                                                |
| ---------------------- | ------------ | ----------- | -------- | ----------------- | -------------------------------- | ------------------------------------------------------------------- |
| id                     | BIGINT       | PK          | NO       |                   | Club identifier                  |                                                                     |
| name                   | VARCHAR(100) |             | NO       |                   | Official club name               |                                                                     |
| short_name             | VARCHAR(30)  |             | NO       |                   | Short club name                  | Used in compact views                                               |
| code                   | VARCHAR(20)  | UNIQUE      | NO       |                   | Club code                        | Used for API and CSV mapping                                        |
| league                 | VARCHAR(50)  |             | NO       |                   | League name                      | MVP currently assumes EPL                                           |
| country                | VARCHAR(50)  |             | NO       |                   | Country                          |                                                                     |
| competition_tier       | VARCHAR(30)  |             | NO       |                   | Competitive tier                 | `DYNASTY`, `TITLE_CONTENDER`, `CHALLENGER`, `MID_TABLE`, `SURVIVAL` |
| trend_direction        | VARCHAR(30)  |             | NO       |                   | Trend direction                  | `RISING`, `STABLE`, `REBUILDING`, `DECLINING`                       |
| beginner_accessibility | DECIMAL(5,2) |             | NO       | 0                 | Beginner accessibility           | Used in beginner adjustment logic                                   |
| is_active              | BOOLEAN      |             | NO       | TRUE              | Active flag                      | For future league expansion and visibility control                  |
| logo_url               | VARCHAR(500) |             | YES      | NULL              | Logo URL                         |                                                                     |
| primary_color          | VARCHAR(7)   |             | YES      | NULL              | Primary color                    | HEX color code                                                      |
| secondary_color        | VARCHAR(7)   |             | YES      | NULL              | Secondary color                  | HEX color code                                                      |
| created_at             | TIMESTAMP    |             | NO       | CURRENT_TIMESTAMP | Creation timestamp               |                                                                     |
| updated_at             | TIMESTAMP    |             | NO       | CURRENT_TIMESTAMP | Last update timestamp            |                                                                     |
| is_deleted             | BOOLEAN      |             | NO       | FALSE             | Soft delete flag                 |                                                                     |

---

# club_dna_scores

## Purpose

Stores DNA scores assigned to clubs.

## Columns

| column_name       | data_type    | constraints              | nullable | default_value     | description                    | note                                |
| ----------------- | ------------ | ------------------------ | -------- | ----------------- | ------------------------------ | ----------------------------------- |
| id                | BIGINT       | PK                       | NO       |                   | Club DNA score identifier      |                                     |
| club_id           | BIGINT       | FK -> clubs.id           | NO       |                   | Club identifier                |                                     |
| dna_definition_id | BIGINT       | FK -> dna_definitions.id | NO       |                   | DNA definition identifier      |                                     |
| score             | DECIMAL(5,2) |                          | NO       |                   | Club DNA score                 | Initial MVP scale is 1-5            |
| is_core           | BOOLEAN      |                          | NO       | FALSE             | Core DNA flag                  | Used for core DNA bonus calculation |
| data_version      | VARCHAR(20)  |                          | NO       |                   | Data version                   | Matches `club_data_version`         |
| created_at        | TIMESTAMP    |                          | NO       | CURRENT_TIMESTAMP | Creation timestamp             |                                     |
| updated_at        | TIMESTAMP    |                          | NO       | CURRENT_TIMESTAMP | Last update timestamp          |                                     |
| is_deleted        | BOOLEAN      |                          | NO       | FALSE             | Soft delete flag               |                                     |

## Table Constraints

| constraint_type | columns                                  | name                                | description                                                  |
| --------------- | ---------------------------------------- | ----------------------------------- | ------------------------------------------------------------ |
| UNIQUE          | club_id, dna_definition_id, data_version | uk_club_dna_scores_club_dna_version | Prevent duplicate club DNA rows within the same data version |

---

# club_tags

## Purpose

Stores club identity tags.

## Columns

| column_name   | data_type    | constraints    | nullable | default_value     | description         | note                                                     |
| ------------- | ------------ | -------------- | -------- | ----------------- | ------------------- | -------------------------------------------------------- |
| id            | BIGINT       | PK             | NO       |                   | Tag identifier      |                                                          |
| club_id       | BIGINT       | FK -> clubs.id | NO       |                   | Club identifier     |                                                          |
| tag_name      | VARCHAR(100) |                | NO       |                   | Tag name            | `#` is presentation-only                                 |
| tag_type      | VARCHAR(30)  |                | YES      |                   | Tag category        | `CULTURE`, `HISTORY`, `STYLE`, `PLAYER_DEVELOPMENT`, etc. |
| display_order | INT          |                | NO       | 0                 | Display order       |                                                          |
| is_active     | BOOLEAN      |                | NO       | TRUE              | Active flag         |                                                          |
| created_at    | TIMESTAMP    |                | NO       | CURRENT_TIMESTAMP | Creation timestamp  |                                                          |
| updated_at    | TIMESTAMP    |                | NO       | CURRENT_TIMESTAMP | Last update timestamp |                                                        |
| is_deleted    | BOOLEAN      |                | NO       | FALSE             | Soft delete flag    |                                                          |

---

# assessment_recommendations

## Purpose

Stores recommendation results.

## Columns

| column_name          | data_type    | constraints               | nullable | default_value     | description                    | note                                            |
| -------------------- | ------------ | ------------------------- | -------- | ----------------- | ------------------------------ | ----------------------------------------------- |
| id                   | BIGINT       | PK                        | NO       |                   | Recommendation identifier      |                                                 |
| assessment_id        | BIGINT       | FK -> user_assessments.id | NO       |                   | Assessment identifier          | Aggregate FK                                    |
| club_id              | BIGINT       | FK -> clubs.id            | NO       |                   | Recommended club ID            |                                                 |
| recommendation_rank  | INT          |                           | NO       |                   | Recommendation rank            | Rank within the current stage                   |
| similarity_score     | DECIMAL(6,2) |                           | NO       | 0                 | DNA similarity score           | Cosine similarity based                         |
| core_dna_bonus       | DECIMAL(6,2) |                           | NO       | 0                 | Core DNA bonus                 | Applied when top DNA matches club core DNA      |
| beginner_bonus       | DECIMAL(6,2) |                           | NO       | 0                 | Beginner bonus                 | Based on accessibility and mass appeal          |
| ai_adjust_score      | DECIMAL(6,2) |                           | NO       | 0                 | AI adjustment score            | Limited to the AI refinement stage              |
| final_score          | DECIMAL(6,2) |                           | NO       | 0                 | Final recommendation score     | Sorting basis                                   |
| recommendation_stage | VARCHAR(20)  |                           | NO       |                   | Recommendation stage           | `TOP5`, `FINAL_TOP3`                            |
| reason_summary       | TEXT         |                           | YES      |                   | Recommendation summary         | Result card summary                             |
| explanation_json     | JSONB        |                           | YES      |                   | Detailed explanation payload   | Used for AI explanation and debugging           |
| explanation_version  | VARCHAR(20)  |                           | NO       |                   | Explanation version            | Tracks prompt/template evolution                |
| created_at           | TIMESTAMP    |                           | NO       | CURRENT_TIMESTAMP | Creation timestamp             |                                                 |
| updated_at           | TIMESTAMP    |                           | NO       | CURRENT_TIMESTAMP | Last update timestamp          |                                                 |
| is_deleted           | BOOLEAN      |                           | NO       | FALSE             | Soft delete flag               |                                                 |

## Table Constraints

| constraint_type | columns                                      | name                                                | description                                                     |
| --------------- | -------------------------------------------- | --------------------------------------------------- | --------------------------------------------------------------- |
| UNIQUE          | assessment_id, club_id, recommendation_stage | uk_assessment_recommendations_assessment_club_stage | Prevent duplicate club rows within the same recommendation stage |

---

# assessment_ai_adjustments

## Purpose

Stores AI refinement history.

## Columns

| column_name          | data_type   | constraints               | nullable | default_value     | description                       | note                                                            |
| -------------------- | ----------- | ------------------------- | -------- | ----------------- | --------------------------------- | --------------------------------------------------------------- |
| id                   | BIGINT      | PK                        | NO       |                   | AI adjustment identifier          |                                                                 |
| assessment_id        | BIGINT      | FK -> user_assessments.id | NO       |                   | Assessment identifier             | Aggregate FK                                                    |
| question             | TEXT        |                           | NO       |                   | AI-generated follow-up question   | Based on differences within the candidate set                  |
| answer               | TEXT        |                           | NO       |                   | User answer                       |                                                                 |
| target_club_ids_json | JSONB       |                           | YES      |                   | Candidate club IDs at question time | Ensures the question is tied to the evaluated club set        |
| adjustment_json      | JSONB       |                           | YES      |                   | AI adjustment output              | Limited to the AI refinement stage                             |
| model_name           | VARCHAR(50) |                           | YES      |                   | AI model name                     |                                                                 |
| prompt_version       | VARCHAR(20) |                           | NO       |                   | Prompt version                    | Tracks prompt evolution                                         |
| created_at           | TIMESTAMP   |                           | NO       | CURRENT_TIMESTAMP | Creation timestamp                |                                                                 |
| updated_at           | TIMESTAMP   |                           | NO       | CURRENT_TIMESTAMP | Last update timestamp             |                                                                 |
| is_deleted           | BOOLEAN     |                           | NO       | FALSE             | Soft delete flag                  |                                                                 |

---

## Domain Overview

### User Domain

* users

### Assessment Domain

* user_assessments
* questions
* question_options
* option_score_mappings
* assessment_answers
* assessment_dna_scores

### DNA Domain

* dna_definitions

### Club Domain

* clubs
* club_dna_scores
* club_tags

### Recommendation Domain

* assessment_recommendations
* assessment_ai_adjustments
