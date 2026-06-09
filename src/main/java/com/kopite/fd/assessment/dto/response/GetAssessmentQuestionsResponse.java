package com.kopite.fd.assessment.dto.response;

import com.kopite.fd.assessment.application.result.GetAssessmentQuestionsResult;
import java.util.List;

public record GetAssessmentQuestionsResponse(
        Long assessmentId,
        String questionVersion,
        List<QuestionItem> questions
) {

    public static GetAssessmentQuestionsResponse from(GetAssessmentQuestionsResult result) {
        return new GetAssessmentQuestionsResponse(
                result.assessmentId(),
                result.questionVersion(),
                result.questions().stream()
                        .map(question -> new QuestionItem(
                                question.questionId(),
                                question.questionText(),
                                question.questionType(),
                                question.primaryDnaDefinitionId(),
                                question.displayOrder(),
                                question.options().stream()
                                        .map(option -> new QuestionOptionItem(
                                                option.optionId(),
                                                option.optionText(),
                                                option.displayOrder()
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
    }

    public record QuestionItem(
            Long questionId,
            String questionText,
            String questionType,
            Long primaryDnaDefinitionId,
            Integer displayOrder,
            List<QuestionOptionItem> options
    ) {
    }

    public record QuestionOptionItem(
            Long optionId,
            String optionText,
            Integer displayOrder
    ) {
    }
}
