package com.kopite.fd.assessment.infrastructure.entity;

import com.kopite.fd.assessment.domain.model.AssessmentQuestion;
import com.kopite.fd.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssessmentQuestionJpaEntity extends BaseEntity {

    @Id
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "question_type", length = 20, nullable = false)
    private String questionType;

    @Column(name = "primary_dna_definition_id", nullable = true)
    private Long primaryDnaDefinitionId;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "question_version", nullable = false)
    private Integer questionVersion;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    public AssessmentQuestion toDomain() {
        return new AssessmentQuestion(
                id,
                questionText,
                questionType,
                primaryDnaDefinitionId,
                displayOrder,
                questionVersion == null ? null : String.valueOf(questionVersion)
        );
    }
}
