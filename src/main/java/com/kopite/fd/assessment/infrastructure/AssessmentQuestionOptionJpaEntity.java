package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.AssessmentQuestionOption;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "question_options")
public class AssessmentQuestionOptionJpaEntity {

    @Id
    private Long id;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "option_text", nullable = false)
    private String optionText;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private Boolean active;

    protected AssessmentQuestionOptionJpaEntity() {
    }

    public AssessmentQuestionOption toDomain() {
        return new AssessmentQuestionOption(
                id,
                questionId,
                optionText,
                displayOrder
        );
    }
}
