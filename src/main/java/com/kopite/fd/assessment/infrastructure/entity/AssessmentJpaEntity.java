package com.kopite.fd.assessment.infrastructure.entity;

import com.kopite.fd.assessment.domain.model.Assessment;
import com.kopite.fd.assessment.domain.type.AssessmentStatus;
import com.kopite.fd.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_assessments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssessmentJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = true)
    private Long userId;

    @Column(name = "anonymous_id", length = 100, nullable = true)
    private String anonymousId;

    @Column(name = "public_result_key", length = 100, nullable = true)
    private String publicResultKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private AssessmentStatus status;

    @Column(name = "question_version", nullable = false)
    private Integer questionVersion;

    @Column(name = "algorithm_version", nullable = true)
    private Integer algorithmVersion;

    @Column(name = "club_data_version", nullable = true)
    private Integer clubDataVersion;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at", nullable = true)
    private LocalDateTime completedAt;

    private AssessmentJpaEntity(
            Long id,
            Long userId,
            String anonymousId,
            String publicResultKey,
            AssessmentStatus status,
            Integer questionVersion,
            Integer algorithmVersion,
            Integer clubDataVersion,
            LocalDateTime startedAt,
            LocalDateTime completedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        super(createdAt, updatedAt);
        this.id = id;
        this.userId = userId;
        this.anonymousId = anonymousId;
        this.publicResultKey = publicResultKey;
        this.status = status;
        this.questionVersion = questionVersion;
        this.algorithmVersion = algorithmVersion;
        this.clubDataVersion = clubDataVersion;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
    }

    public static AssessmentJpaEntity fromDomain(Assessment assessment) {
        return new AssessmentJpaEntity(
                assessment.getId(),
                assessment.getUserId(),
                assessment.getAnonymousId(),
                null,
                assessment.getStatus(),
                toInteger(assessment.getQuestionVersion()),
                toInteger(assessment.getAlgorithmVersion()),
                toInteger(assessment.getClubDataVersion()),
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
                toStringValue(questionVersion),
                toStringValue(algorithmVersion),
                toStringValue(clubDataVersion),
                startedAt,
                completedAt,
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    private static Integer toInteger(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Integer.valueOf(value);
    }

    private static String toStringValue(Integer value) {
        if (value == null) {
            return null;
        }

        return String.valueOf(value);
    }
}
