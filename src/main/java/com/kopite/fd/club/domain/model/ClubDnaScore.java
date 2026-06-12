package com.kopite.fd.club.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ClubDnaScore {

    private static final BigDecimal MIN_SCORE = new BigDecimal("1.00");
    private static final BigDecimal MAX_SCORE = new BigDecimal("5.00");

    private final Long id;
    private final Long clubId;
    private final Long dnaDefinitionId;
    private final BigDecimal score;
    private final Boolean core;
    private final String dataVersion;
    private final String dnaCategory;
    private final String dnaKey;
    private final String dnaDisplayName;
    private final Integer dnaDisplayOrder;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ClubDnaScore(
            Long id,
            Long clubId,
            Long dnaDefinitionId,
            BigDecimal score,
            Boolean core,
            String dataVersion,
            String dnaCategory,
            String dnaKey,
            String dnaDisplayName,
            Integer dnaDisplayOrder,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        validateScore(score);
        validateDataVersion(dataVersion);
        this.id = id;
        this.clubId = clubId;
        this.dnaDefinitionId = dnaDefinitionId;
        this.score = score;
        this.core = core;
        this.dataVersion = dataVersion;
        this.dnaCategory = dnaCategory;
        this.dnaKey = dnaKey;
        this.dnaDisplayName = dnaDisplayName;
        this.dnaDisplayOrder = dnaDisplayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getClubId() {
        return clubId;
    }

    public Long getDnaDefinitionId() {
        return dnaDefinitionId;
    }

    public BigDecimal getScore() {
        return score;
    }

    public Boolean isCore() {
        return core;
    }

    public String getDataVersion() {
        return dataVersion;
    }

    public String getDnaCategory() {
        return dnaCategory;
    }

    public String getDnaKey() {
        return dnaKey;
    }

    public String getDnaDisplayName() {
        return dnaDisplayName;
    }

    public Integer getDnaDisplayOrder() {
        return dnaDisplayOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    private void validateScore(BigDecimal score) {
        if (score == null) {
            throw new IllegalArgumentException("score must not be null.");
        }
        if (score.compareTo(MIN_SCORE) < 0 || score.compareTo(MAX_SCORE) > 0) {
            throw new IllegalArgumentException("score must be between 1.00 and 5.00.");
        }
    }

    private void validateDataVersion(String dataVersion) {
        if (dataVersion == null || dataVersion.isBlank()) {
            throw new IllegalArgumentException("dataVersion must not be blank.");
        }
    }
}
