package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.AssessmentDnaScore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_dna_scores")
public class AssessmentDnaScoreJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Column(name = "dna_definition_id", nullable = false)
    private Long dnaDefinitionId;

    @Column(nullable = false)
    private int score;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected AssessmentDnaScoreJpaEntity() {
    }

    private AssessmentDnaScoreJpaEntity(
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

    public static AssessmentDnaScoreJpaEntity fromDomain(AssessmentDnaScore assessmentDnaScore) {
        return new AssessmentDnaScoreJpaEntity(
                assessmentDnaScore.getId(),
                assessmentDnaScore.getAssessmentId(),
                assessmentDnaScore.getDnaDefinitionId(),
                assessmentDnaScore.getScore(),
                assessmentDnaScore.getCreatedAt(),
                assessmentDnaScore.getUpdatedAt()
        );
    }

    public AssessmentDnaScore toDomain() {
        return new AssessmentDnaScore(
                id,
                assessmentId,
                dnaDefinitionId,
                score,
                createdAt,
                updatedAt
        );
    }
}
