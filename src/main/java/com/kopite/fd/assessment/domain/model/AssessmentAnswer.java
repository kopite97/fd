package com.kopite.fd.assessment.domain.model;

import java.time.LocalDateTime;

public class AssessmentAnswer {

    private final Long id;
    private final Long assessmentId;
    private final Long questionId;
    private final Long optionId;
    private final String answerText;
    private final String scoreSnapshotJson;
    private final LocalDateTime createdAt;

    public AssessmentAnswer(
            Long id,
            Long assessmentId,
            Long questionId,
            Long optionId,
            String answerText,
            String scoreSnapshotJson,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.assessmentId = assessmentId;
        this.questionId = questionId;
        this.optionId = optionId;
        this.answerText = answerText;
        this.scoreSnapshotJson = scoreSnapshotJson;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public String getScoreSnapshotJson() {
        return scoreSnapshotJson;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
