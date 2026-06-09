package com.kopite.fd.assessment.dto.request;

import jakarta.validation.constraints.NotNull;

public record SubmitAssessmentAnswerRequest(
        @NotNull Long questionId,
        Long optionId,
        String answerText
) {
}
