package com.kopite.fd.assessment.application;

import com.kopite.fd.assessment.domain.AssessmentStatus;
import java.time.LocalDateTime;

public record StartAssessmentResult(
        Long assessmentId,
        Long userId,
        String anonymousId,
        AssessmentStatus status,
        String questionVersion,
        LocalDateTime startedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
