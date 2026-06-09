package com.kopite.fd.assessment.infrastructure.entity;

import com.kopite.fd.assessment.domain.model.OptionScoreMapping;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "option_score_mappings")
public class OptionScoreMappingJpaEntity {

    @Id
    private Long id;

    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(name = "dna_definition_id", nullable = false)
    private Long dnaDefinitionId;

    @Column(name = "score_delta", nullable = false)
    private int scoreDelta;

    protected OptionScoreMappingJpaEntity() {
    }

    public OptionScoreMapping toDomain() {
        return new OptionScoreMapping(
                id,
                optionId,
                dnaDefinitionId,
                scoreDelta
        );
    }
}
