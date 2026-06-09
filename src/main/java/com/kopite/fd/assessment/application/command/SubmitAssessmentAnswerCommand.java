package com.kopite.fd.assessment.application.command;

public record SubmitAssessmentAnswerCommand(
        Long assessmentId,
        Long questionId,
        Long optionId,
        String answerText
) {
}
