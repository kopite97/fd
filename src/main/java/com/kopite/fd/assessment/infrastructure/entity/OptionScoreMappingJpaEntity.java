package com.kopite.fd.assessment.infrastructure.entity;

import com.kopite.fd.assessment.domain.model.OptionScoreMapping;
import com.kopite.fd.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "option_score_mappings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionScoreMappingJpaEntity extends BaseEntity {

    @Id
    private Long id;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(name = "dna_definition_id", nullable = false)
    private Long dnaDefinitionId;

    @Column(name = "score_delta", precision = 5, scale = 2, nullable = false)
    private BigDecimal scoreDelta;

    public OptionScoreMapping toDomain() {
        return new OptionScoreMapping(
                id,
                optionId,
                dnaDefinitionId,
                scoreDelta.intValue()
        );
    }
}
