package com.kopite.fd.assessment.application;

import java.util.List;

public record GetAssessmentQuestionsResult(
        Long assessmentId,
        String questionVersion,
        List<AssessmentQuestionItem> questions
) {

    public record AssessmentQuestionItem(
            Long questionId,
            String questionText,
            String questionType,
            Long primaryDnaDefinitionId,
            Integer displayOrder,
            List<AssessmentQuestionOptionItem> options
    ) {
    }

    public record AssessmentQuestionOptionItem(
            Long optionId,
            String optionText,
            Integer displayOrder
    ) {
    }
}
