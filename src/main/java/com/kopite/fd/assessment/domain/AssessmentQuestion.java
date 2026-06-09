package com.kopite.fd.assessment.domain;

public class AssessmentQuestion {

    private final Long id;
    private final String questionText;
    private final String questionType;
    private final Long primaryDnaDefinitionId;
    private final Integer displayOrder;
    private final String questionVersion;

    public AssessmentQuestion(
            Long id,
            String questionText,
            String questionType,
            Long primaryDnaDefinitionId,
            Integer displayOrder,
            String questionVersion
    ) {
        this.id = id;
        this.questionText = questionText;
        this.questionType = questionType;
        this.primaryDnaDefinitionId = primaryDnaDefinitionId;
        this.displayOrder = displayOrder;
        this.questionVersion = questionVersion;
    }

    public Long getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getQuestionType() {
        return questionType;
    }

    public Long getPrimaryDnaDefinitionId() {
        return primaryDnaDefinitionId;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public String getQuestionVersion() {
        return questionVersion;
    }
}
