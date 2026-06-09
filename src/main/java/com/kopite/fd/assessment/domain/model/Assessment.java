package com.kopite.fd.assessment.domain.model;

import java.time.LocalDateTime;
import com.kopite.fd.assessment.domain.type.AssessmentStatus;

public class Assessment {

    private final Long id;
    private final Long userId;
    private final String anonymousId;
    private final AssessmentStatus status;
    private final String questionVersion;
    private final String algorithmVersion;
    private final String clubDataVersion;
    private final LocalDateTime startedAt;
    private final LocalDateTime completedAt;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Assessment(
            Long id,
            Long userId,
            String anonymousId,
            AssessmentStatus status,
            String questionVersion,
            String algorithmVersion,
            String clubDataVersion,
            LocalDateTime startedAt,
            LocalDateTime completedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.userId = userId;
        this.anonymousId = anonymousId;
        this.status = status;
        this.questionVersion = questionVersion;
        this.algorithmVersion = algorithmVersion;
        this.clubDataVersion = clubDataVersion;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAnonymousId() {
        return anonymousId;
    }

    public AssessmentStatus getStatus() {
        return status;
    }

    public String getQuestionVersion() {
        return questionVersion;
    }

    public String getAlgorithmVersion() {
        return algorithmVersion;
    }

    public String getClubDataVersion() {
        return clubDataVersion;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
