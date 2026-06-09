# Football DNA Recommendation System

## Overview

Football DNA recommends clubs based on user preferences rather than club strength.

The recommendation system consists of four major datasets:

* Emotional DNA
* Playstyle DNA
* Club Status
* Club Tags

---

## Emotional DNA

Measures the types of stories, identities, and club cultures a user prefers.

Each attribute is scored from 1 to 5.

### Attributes

* Club Prestige
* Fan Culture
* Underdog
* Growth
* Star Power
* Drama
* Local Identity
* Popularity

---

## Playstyle DNA

Measures football style preferences.

Unlike Emotional DNA, Playstyle DNA should be managed using objective football data whenever possible.

### Attributes

* Possession
* Directness
* Pressing
* Organization
* Creativity
* Transition Speed

---

## Club Status

Used for explanation and presentation.

Not used directly in recommendation scoring.

### Competitive Stage

* Dynasty
* Title Contender
* Challenger
* Mid-table
* Relegation Battle

### Direction

* Rising
* Stable
* Rebuilding
* Declining

---

## Club Tags

Club Tags are managed separately from DNA scores.

Examples:

### Liverpool

* #YNWA
* #Anfield
* #TheKop
* #ComebackKings

### Brighton

* #Underdog
* #PlayerDevelopment
* #DataDriven
* #GrowingClub

---

## Question Design Principles

### Accessibility

Questions must be answerable by users with little football knowledge.

### Balanced Choices

Both choices should be attractive.

### Preference-Based

Questions measure preference rather than correctness.

### Club Neutrality

Questions should not immediately suggest a specific club.

### Match Expression

Playstyle questions should describe match situations rather than tactical terminology.

---

## Assessment Structure

### Emotional DNA

* 8 attributes
* 2 questions per attribute
* 16 questions total

### Playstyle DNA

* 6 attributes
* 2 questions per attribute
* 12 questions total

### Total

* 28 questions

---

## Recommendation Algorithm

### Step 1

Collect assessment responses.

### Step 2

Generate Emotional DNA.

### Step 3

Generate Playstyle DNA.

### Step 4

Calculate similarity between user DNA and club DNA.

### Step 5

Apply Core DNA Bonus.

### Step 6

Apply Beginner Adjustment.

### Step 7

Generate Top 5 candidates.

---

## AI Refinement

AI does not recommend clubs.

AI performs candidate refinement only.

Process:

```text
TOP5 Candidates
↓
Candidate Analysis
↓
Custom Questions
↓
User Response
↓
Final TOP3
```

### Constraints

* No new club recommendations
* TOP5 candidates only
* Maximum ±5 point adjustment

---

## Final Result Generation

The final explanation is generated using:

* User Emotional DNA
* User Playstyle DNA
* Club DNA
* Competitive Stage
* Direction
* Club Tags

The goal is not only to recommend a club, but to explain why the club matches the user.
