package com.kopite.fd.assessment.domain.model;

import java.time.LocalDateTime;

public class AssessmentDnaScore {

    private final Long id;
    private final Long assessmentId;
    private final Long dnaDefinitionId;
    private final int score;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public AssessmentDnaScore(
            Long id,
            Long assessmentId,
            Long dnaDefinitionId,
            int score,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.assessmentId = assessmentId;
        this.dnaDefinitionId = dnaDefinitionId;
        this.score = score;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public Long getDnaDefinitionId() {
        return dnaDefinitionId;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
