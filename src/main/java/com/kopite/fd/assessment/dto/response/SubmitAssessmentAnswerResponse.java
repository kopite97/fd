package com.kopite.fd.assessment.dto.response;

import com.kopite.fd.assessment.application.SubmitAssessmentAnswerResult;
import java.time.LocalDateTime;

public record SubmitAssessmentAnswerResponse(
        Long answerId,
        Long assessmentId,
        Long questionId,
        Long optionId,
        String answerText,
        LocalDateTime createdAt
) {

    public static SubmitAssessmentAnswerResponse from(SubmitAssessmentAnswerResult result) {
        return new SubmitAssessmentAnswerResponse(
                result.answerId(),
                result.assessmentId(),
                result.questionId(),
                result.optionId(),
                result.answerText(),
                result.createdAt()
        );
    }
}
