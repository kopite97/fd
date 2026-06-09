package com.kopite.fd.assessment.domain.model;

public class AssessmentQuestionOption {

    private final Long id;
    private final Long questionId;
    private final String optionText;
    private final Integer displayOrder;

    public AssessmentQuestionOption(
            Long id,
            Long questionId,
            String optionText,
            Integer displayOrder
    ) {
        this.id = id;
        this.questionId = questionId;
        this.optionText = optionText;
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getOptionText() {
        return optionText;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}
