package com.kopite.fd.assessment.application;

import com.kopite.fd.assessment.domain.Assessment;
import com.kopite.fd.assessment.domain.AssessmentRepository;
import com.kopite.fd.assessment.domain.AssessmentStatus;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StartAssessmentService {

    private final AssessmentRepository assessmentRepository;

    public StartAssessmentService(AssessmentRepository assessmentRepository) {
        this.assessmentRepository = assessmentRepository;
    }

    @Transactional
    public StartAssessmentResult start(StartAssessmentCommand command) {
        validate(command);

        LocalDateTime now = LocalDateTime.now();

        Assessment assessment = new Assessment(
                null,
                command.userId(),
                normalizeAnonymousId(command.anonymousId()),
                AssessmentStatus.IN_PROGRESS,
                command.questionVersion(),
                null,
                null,
                now,
                null,
                now,
                now
        );

        Assessment savedAssessment = assessmentRepository.save(assessment);

        return new StartAssessmentResult(
                savedAssessment.getId(),
                savedAssessment.getUserId(),
                savedAssessment.getAnonymousId(),
                savedAssessment.getStatus(),
                savedAssessment.getQuestionVersion(),
                savedAssessment.getStartedAt(),
                savedAssessment.getCreatedAt(),
                savedAssessment.getUpdatedAt()
        );
    }

    private void validate(StartAssessmentCommand command) {
        boolean hasUserId = command.userId() != null;
        boolean hasAnonymousId = hasText(command.anonymousId());

        if (!hasUserId && !hasAnonymousId) {
            throw new IllegalArgumentException("At least one of userId or anonymousId must be present.");
        }

        if (!hasText(command.questionVersion())) {
            throw new IllegalArgumentException("questionVersion must be present.");
        }
    }

    private String normalizeAnonymousId(String anonymousId) {
        if (!hasText(anonymousId)) {
            return null;
        }

        return anonymousId.trim();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
