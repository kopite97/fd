package com.kopite.fd.assessment.application;

public record CompleteAssessmentCommand(
        Long assessmentId,
        String algorithmVersion
) {
}
