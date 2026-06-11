package com.kopite.fd.assessment.infrastructure.entity;

import com.kopite.fd.assessment.domain.model.AssessmentDnaScore;
import com.kopite.fd.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "assessment_dna_scores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssessmentDnaScoreJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Column(name = "dna_definition_id", nullable = false)
    private Long dnaDefinitionId;

    @Column(name = "score", precision = 5, scale = 2, nullable = false)
    private BigDecimal score;

    private AssessmentDnaScoreJpaEntity(
            Long id,
            Long assessmentId,
            Long dnaDefinitionId,
            BigDecimal score,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        super(createdAt, updatedAt, false);
        this.id = id;
        this.assessmentId = assessmentId;
        this.dnaDefinitionId = dnaDefinitionId;
        this.score = score;
    }

    public static AssessmentDnaScoreJpaEntity fromDomain(AssessmentDnaScore assessmentDnaScore) {
        return new AssessmentDnaScoreJpaEntity(
                assessmentDnaScore.getId(),
                assessmentDnaScore.getAssessmentId(),
                assessmentDnaScore.getDnaDefinitionId(),
                BigDecimal.valueOf(assessmentDnaScore.getScore()),
                assessmentDnaScore.getCreatedAt(),
                assessmentDnaScore.getUpdatedAt()
        );
    }

    public AssessmentDnaScore toDomain() {
        return new AssessmentDnaScore(
                id,
                assessmentId,
                dnaDefinitionId,
                score.intValue(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }
}
