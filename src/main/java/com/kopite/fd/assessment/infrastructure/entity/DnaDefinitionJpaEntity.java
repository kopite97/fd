package com.kopite.fd.assessment.infrastructure.entity;

import com.kopite.fd.assessment.domain.model.DnaDefinition;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "dna_definitions")
public class DnaDefinitionJpaEntity {

    @Id
    private Long id;

    @Column(name = "dna_category", nullable = false)
    private String dnaCategory;

    @Column(name = "dna_key", nullable = false)
    private String dnaKey;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column
    private String description;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "is_active")
    private Boolean active;

    protected DnaDefinitionJpaEntity() {
    }

    public DnaDefinition toDomain() {
        return new DnaDefinition(
                id,
                dnaCategory,
                dnaKey,
                displayName,
                description,
                displayOrder
        );
    }
}
