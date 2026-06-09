# Football DNA Data

## Overview

Football DNA Data is the master dataset used to generate club recommendations.

The dataset is maintained in Google Sheets and serves as the source of truth for club-related recommendation data.

---

## Data Ownership

Football DNA Data is not a database schema.

It is the primary source dataset used to manage recommendation-related club data.

The backend database stores transformed and normalized data derived from this dataset.

Data flow:

```text
Football DNA Data (Google Sheet)
    ↓
Import / Transformation
    ↓
Database
    ↓
Recommendation Engine
```

Changes should be made in Football DNA Data first and then synchronized to the database.

The database should not be treated as the primary source of recommendation data.

---

## Purpose

The dataset provides:

* Club information
* Emotional DNA scores
* Playstyle DNA scores
* Club status information
* Club tags

This data is used by the recommendation engine to calculate club compatibility scores.

---

## Supported Clubs

Current MVP scope:

* English Premier League clubs only

Future versions may include:

* La Liga
* Bundesliga
* Serie A

---

## Emotional DNA

Emotional DNA represents the emotional identity of a football club.

Each attribute is scored on a predefined scale.

### Attributes

| DNA Key        | Description                                   |
| -------------- | --------------------------------------------- |
| club_prestige  | Historical prestige and reputation            |
| fan_culture    | Strength of supporter culture                 |
| underdog       | Underdog identity                             |
| growth         | Growth potential and future outlook           |
| star_power     | Presence of star players and personalities    |
| drama          | Likelihood of dramatic moments and narratives |
| local_identity | Connection to local community and history     |
| popularity     | Global popularity and recognition             |

---

## Playstyle DNA

Playstyle DNA represents how a club plays football.

Whenever possible, scores should be derived from objective football data.

### Attributes

| DNA Key          | Description                              |
| ---------------- | ---------------------------------------- |
| possession       | Preference for possession football       |
| directness       | Preference for direct attacking football |
| pressing         | Intensity of pressing                    |
| organization     | Tactical structure and discipline        |
| creativity       | Creative freedom in attacking play       |
| transition_speed | Speed of attacking transitions           |

---

## Club Status

Club Status provides contextual information used for recommendation explanations.

Status values are not intended to be primary recommendation factors.

### Competitive Stage

| Value             | Description                                  |
| ----------------- | -------------------------------------------- |
| Dynasty           | Dominant club with sustained success         |
| Title Contender   | Consistent title challenger                  |
| Challenger        | Competitive club aiming for higher positions |
| Mid-table         | Stable middle-table club                     |
| Relegation Battle | Club fighting against relegation             |

### Direction

| Value      | Description                         |
| ---------- | ----------------------------------- |
| Rising     | Positive long-term trajectory       |
| Stable     | Relatively stable position          |
| Rebuilding | Undergoing major structural changes |
| Declining  | Negative long-term trajectory       |

---

## Club Tags

Club Tags represent unique identities, narratives, and community culture.

Examples:

### Liverpool

* YNWA
* Anfield
* The Kop
* Comeback Kings

### Brighton

* Underdog
* Data Driven
* Player Development
* Growing Club

Tags are primarily used for explanation generation rather than recommendation scoring.

---

## Recommendation Usage

The recommendation engine primarily uses:

* Emotional DNA
* Playstyle DNA
* Core DNA values

The explanation engine additionally uses:

* Club Status
* Club Tags

---
## Synchronization Strategy

Football DNA Data is currently maintained locally.

Future versions will retrieve the dataset through external APIs.

Regardless of the transport mechanism, Football DNA Data remains the source of truth for recommendation-related club data.

The synchronization process is responsible for:

- Retrieving source data
- Validating data integrity
- Transforming data into database entities
- Updating database records

---

## Source of Truth

Football DNA Data is the source of truth for recommendation-related club data.

The backend database stores transformed copies of this data for application use.

Recommendation data should be maintained in Football DNA Data and synchronized to the database through the import process.

Direct database edits should be avoided whenever possible.
