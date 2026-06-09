package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.AssessmentQuestion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "questions")
public class AssessmentQuestionJpaEntity {

    @Id
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "question_type")
    private String questionType;

    @Column(name = "primary_dna_definition_id")
    private Long primaryDnaDefinitionId;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "question_version")
    private String questionVersion;

    @Column(name = "is_active")
    private Boolean active;

    protected AssessmentQuestionJpaEntity() {
    }

    public AssessmentQuestion toDomain() {
        return new AssessmentQuestion(
                id,
                questionText,
                questionType,
                primaryDnaDefinitionId,
                displayOrder,
                questionVersion
        );
    }
}
