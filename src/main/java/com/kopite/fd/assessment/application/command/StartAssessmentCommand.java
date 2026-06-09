package com.kopite.fd.assessment.application.command;

public record StartAssessmentCommand(
        Long userId,
        String anonymousId,
        String questionVersion
) {
}
