package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.Assessment;
import com.kopite.fd.assessment.domain.AssessmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_assessments")
public class AssessmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "anonymous_id")
    private String anonymousId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssessmentStatus status;

    @Column(name = "question_version")
    private String questionVersion;

    @Column(name = "algorithm_version")
    private String algorithmVersion;

    @Column(name = "club_data_version")
    private String clubDataVersion;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected AssessmentJpaEntity() {
    }

    private AssessmentJpaEntity(
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

    public static AssessmentJpaEntity fromDomain(Assessment assessment) {
        return new AssessmentJpaEntity(
                assessment.getId(),
                assessment.getUserId(),
                assessment.getAnonymousId(),
                assessment.getStatus(),
                assessment.getQuestionVersion(),
                assessment.getAlgorithmVersion(),
                assessment.getClubDataVersion(),
                assessment.getStartedAt(),
                assessment.getCompletedAt(),
                assessment.getCreatedAt(),
                assessment.getUpdatedAt()
        );
    }

    public Assessment toDomain() {
        return new Assessment(
                id,
                userId,
                anonymousId,
                status,
                questionVersion,
                algorithmVersion,
                clubDataVersion,
                startedAt,
                completedAt,
                createdAt,
                updatedAt
        );
    }
}
