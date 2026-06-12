package com.kopite.fd.dna.infrastructure.entity;

import com.kopite.fd.dna.domain.model.DnaDefinition;
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
@Table(name = "dna_definitions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DnaDefinitionJpaEntity extends BaseEntity {

    @Id
    private Long id;

    @Column(name = "dna_category", length = 20, nullable = false)
    private String dnaCategory;

    @Column(name = "dna_key", length = 50, nullable = false)
    private String dnaKey;

    @Column(name = "display_name", length = 50, nullable = false)
    private String displayName;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

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
