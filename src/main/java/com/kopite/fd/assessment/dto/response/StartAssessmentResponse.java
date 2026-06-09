package com.kopite.fd.assessment.dto.response;

import com.kopite.fd.assessment.application.result.StartAssessmentResult;
import com.kopite.fd.assessment.domain.type.AssessmentStatus;
import java.time.LocalDateTime;

public record StartAssessmentResponse(
        Long assessmentId,
        Long userId,
        String anonymousId,
        AssessmentStatus status,
        String questionVersion,
        LocalDateTime startedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static StartAssessmentResponse from(StartAssessmentResult result) {
        return new StartAssessmentResponse(
                result.assessmentId(),
                result.userId(),
                result.anonymousId(),
                result.status(),
                result.questionVersion(),
                result.startedAt(),
                result.createdAt(),
                result.updatedAt()
        );
    }
}
