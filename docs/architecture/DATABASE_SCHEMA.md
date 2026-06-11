# Database Schema

## Overview

This document describes the database tables used by Football DNA.

All tables inherit common audit columns through a shared base entity.

---

## Common Audit Columns

The following columns are inherited by all tables.

| Column     | Description           |
| ---------- | --------------------- |
| created_at | Creation timestamp    |
| updated_at | Last update timestamp |

---

# users

## Purpose

Stores registered users.

## Columns

| Column           | Description                       |
| ---------------- | --------------------------------- |
| id               | User identifier                   |
| email            | User email address                |
| nickname         | User nickname                     |
| provider         | OAuth provider                    |
| provider_user_id | Provider-specific user identifier |

---

# user_assessments

## Purpose

Represents a single Football DNA assessment session.

## Columns

| Column            | Description                      |
| ----------------- | -------------------------------- |
| id                | Assessment identifier            |
| user_id           | Associated user                  |
| anonymous_id      | Anonymous user identifier        |
| public_result_key | Public sharing key               |
| status            | Assessment status                |
| question_version  | Question set version             |
| algorithm_version | Recommendation algorithm version |
| club_data_version | Club data version                |
| started_at        | Assessment start time            |
| completed_at      | Assessment completion time       |

---

# dna_definitions

## Purpose

Master table for all DNA attributes.

## Columns

| Column        | Description                          |
| ------------- | ------------------------------------ |
| id            | DNA definition identifier            |
| dna_category  | DNA category (Emotional / Playstyle) |
| dna_key       | Internal DNA key                     |
| display_name  | Display name                         |
| description   | DNA description                      |
| display_order | Display order                        |
| is_active     | Active flag                          |

---

# questions

## Purpose

Stores assessment questions.

## Columns

| Column                    | Description         |
| ------------------------- | ------------------- |
| id                        | Question identifier |
| question_text             | Question content    |
| question_type             | Question type       |
| primary_dna_definition_id | Primary DNA target  |
| display_order             | Display order       |
| question_version          | Question version    |
| is_active                 | Active flag         |

---

# question_options

## Purpose

Stores selectable options for questions.

## Columns

| Column        | Description         |
| ------------- | ------------------- |
| id            | Option identifier   |
| question_id   | Associated question |
| option_text   | Option content      |
| display_order | Display order       |
| is_active     | Active flag         |

---

# option_score_mappings

## Purpose

Maps selected options to DNA score changes.

## Columns

| Column            | Description          |
| ----------------- | -------------------- |
| id                | Mapping identifier   |
| option_id         | Related option       |
| dna_definition_id | Target DNA           |
| score_delta       | DNA score adjustment |

---

# assessment_answers

## Purpose

Stores user responses.

## Columns

| Column              | Description                   |
| ------------------- | ----------------------------- |
| id                  | Answer identifier             |
| assessment_id       | Assessment identifier         |
| question_id         | Question identifier           |
| option_id           | Selected option               |
| answer_text         | Free-text answer              |
| score_snapshot_json | Score snapshot at answer time |

---

# assessment_dna_scores

## Purpose

Stores calculated DNA scores for an assessment.

## Columns

| Column            | Description           |
| ----------------- | --------------------- |
| id                | DNA score identifier  |
| assessment_id     | Assessment identifier |
| dna_definition_id | DNA definition        |
| score             | Calculated DNA score  |

---

# clubs

## Purpose

Stores football club information.

## Columns

| Column                 | Description           |
| ---------------------- | --------------------- |
| id                     | Club identifier       |
| name                   | Club name             |
| short_name             | Short club name       |
| code                   | Club code             |
| league                 | League name           |
| country                | Country               |
| competition_tier       | Competitive level     |
| trend_direction        | Club direction        |
| beginner_accessibility | Beginner friendliness |
| is_active              | Active Flag           |
| logo_url               | Logo Image URL        |
| primary_color          | HEX code              |
| secondary_color        | HEX code              |   

---

# club_dna_scores

## Purpose

Stores DNA scores assigned to clubs.

## Columns

| Column            | Description               |
| ----------------- | ------------------------- |
| id                | Club DNA score identifier |
| club_id           | Club identifier           |
| dna_definition_id | DNA definition            |
| score             | DNA score                 |
| is_core           | Core DNA flag             |
| data_version      | Data version              |

---

# club_tags

## Purpose

Stores club identity tags.

## Columns

| Column        | Description     |
| ------------- | --------------- |
| id            | Tag identifier  |
| club_id       | Club identifier |
| tag_name      | Tag name        |
| tag_type      | Tag category    |
| display_order | Display order   |
| is_active     | Active Flag     |

---

# assessment_recommendations

## Purpose

Stores recommendation results.

## Columns

| Column               | Description                  |
| -------------------- | ---------------------------- |
| id                   | Recommendation identifier    |
| assessment_id        | Assessment identifier        |
| club_id              | Recommended club             |
| recommendation_rank  | Recommendation ranking       |
| similarity_score     | DNA similarity score         |
| core_dna_bonus       | Core DNA bonus               |
| beginner_bonus       | Beginner bonus               |
| ai_adjust_score      | AI adjustment score          |
| final_score          | Final recommendation score   |
| recommendation_stage | Recommendation stage         |
| reason_summary       | Recommendation summary       |
| explanation_json     | Detailed explanation payload |
| explanation_version  | Explanation version          |

---

# assessment_ai_adjustments

## Purpose

Stores AI refinement history.

## Columns

| Column          | Description              |
| --------------- | ------------------------ |
| id              | AI adjustment identifier |
| assessment_id   | Assessment identifier    |
| question        | AI-generated question    |
| answer          | User answer              |
| adjustment_json | Score adjustment data    |
| model_name      | AI model name            |

---

## Domain Overview

### User Domain

* users
* user_assessments

### Assessment Domain

* questions
* question_options
* assessment_answers

### DNA Domain

* dna_definitions
* option_score_mappings
* assessment_dna_scores

### Club Domain

* clubs
* club_dna_scores
* club_tags

### Recommendation Domain

* assessment_recommendations
* assessment_ai_adjustments
