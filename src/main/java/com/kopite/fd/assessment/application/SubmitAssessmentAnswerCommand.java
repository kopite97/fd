package com.kopite.fd.assessment.application;

public record SubmitAssessmentAnswerCommand(
        Long assessmentId,
        Long questionId,
        Long optionId,
        String answerText
) {
}
