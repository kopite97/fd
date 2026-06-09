package com.kopite.fd.assessment.infrastructure.entity;

import com.kopite.fd.assessment.domain.model.AssessmentAnswer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_answers")
public class AssessmentAnswerJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "option_id")
    private Long optionId;

    @Column(name = "answer_text")
    private String answerText;

    @Column(name = "score_snapshot_json")
    private String scoreSnapshotJson;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected AssessmentAnswerJpaEntity() {
    }

    private AssessmentAnswerJpaEntity(
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

    public static AssessmentAnswerJpaEntity fromDomain(AssessmentAnswer assessmentAnswer) {
        return new AssessmentAnswerJpaEntity(
                assessmentAnswer.getId(),
                assessmentAnswer.getAssessmentId(),
                assessmentAnswer.getQuestionId(),
                assessmentAnswer.getOptionId(),
                assessmentAnswer.getAnswerText(),
                assessmentAnswer.getScoreSnapshotJson(),
                assessmentAnswer.getCreatedAt()
        );
    }

    public AssessmentAnswer toDomain() {
        return new AssessmentAnswer(
                id,
                assessmentId,
                questionId,
                optionId,
                answerText,
                scoreSnapshotJson,
                createdAt
        );
    }
}
