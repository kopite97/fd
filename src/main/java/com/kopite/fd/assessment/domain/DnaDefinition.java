package com.kopite.fd.assessment.domain;

public class DnaDefinition {

    private final Long id;
    private final String dnaCategory;
    private final String dnaKey;
    private final String displayName;
    private final String description;
    private final Integer displayOrder;

    public DnaDefinition(
            Long id,
            String dnaCategory,
            String dnaKey,
            String displayName,
            String description,
            Integer displayOrder
    ) {
        this.id = id;
        this.dnaCategory = dnaCategory;
        this.dnaKey = dnaKey;
        this.displayName = displayName;
        this.description = description;
        this.displayOrder = displayOrder;
    }

    public Long getId() {
        return id;
    }

    public String getDnaCategory() {
        return dnaCategory;
    }

    public String getDnaKey() {
        return dnaKey;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }
}
