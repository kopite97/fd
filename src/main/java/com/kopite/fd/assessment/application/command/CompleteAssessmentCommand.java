package com.kopite.fd.assessment.application.command;

public record CompleteAssessmentCommand(
        Long assessmentId,
        String algorithmVersion
) {
}
