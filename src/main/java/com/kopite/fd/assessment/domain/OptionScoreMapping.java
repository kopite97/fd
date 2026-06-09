package com.kopite.fd.assessment.domain;

public class OptionScoreMapping {

    private final Long id;
    private final Long optionId;
    private final Long dnaDefinitionId;
    private final int scoreDelta;

    public OptionScoreMapping(
            Long id,
            Long optionId,
            Long dnaDefinitionId,
            int scoreDelta
    ) {
        this.id = id;
        this.optionId = optionId;
        this.dnaDefinitionId = dnaDefinitionId;
        this.scoreDelta = scoreDelta;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public Long getDnaDefinitionId() {
        return dnaDefinitionId;
    }

    public int getScoreDelta() {
        return scoreDelta;
    }
}
