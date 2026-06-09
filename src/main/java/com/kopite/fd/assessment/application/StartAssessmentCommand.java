package com.kopite.fd.assessment.application;

public record StartAssessmentCommand(
        Long userId,
        String anonymousId,
        String questionVersion
) {
}
