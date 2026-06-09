package com.kopite.fd.assessment.application;

import java.time.LocalDateTime;

public record SubmitAssessmentAnswerResult(
        Long answerId,
        Long assessmentId,
        Long questionId,
        Long optionId,
        String answerText,
        LocalDateTime createdAt
) {
}
