package com.kopite.fd.assessment.infrastructure.entity;

import com.kopite.fd.assessment.domain.model.AssessmentQuestionOption;
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
@Table(name = "question_options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AssessmentQuestionOptionJpaEntity extends BaseEntity {

    @Id
    private Long id;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "option_text", nullable = false)
    private String optionText;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    public AssessmentQuestionOption toDomain() {
        return new AssessmentQuestionOption(
                id,
                questionId,
                optionText,
                displayOrder
        );
    }
}
