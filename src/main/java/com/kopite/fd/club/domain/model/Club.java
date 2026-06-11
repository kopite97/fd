package com.kopite.fd.club.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Club {

    private final Long id;
    private final String name;
    private final String shortName;
    private final String code;
    private final String league;
    private final String country;
    private final String competitionTier;
    private final String trendDirection;
    private final BigDecimal beginnerAccessibility;
    private final Boolean active;
    private final String logoUrl;
    private final String primaryColor;
    private final String secondaryColor;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Club(
            Long id,
            String name,
            String shortName,
            String code,
            String league,
            String country,
            String competitionTier,
            String trendDirection,
            BigDecimal beginnerAccessibility,
            Boolean active,
            String logoUrl,
            String primaryColor,
            String secondaryColor,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
        this.code = code;
        this.league = league;
        this.country = country;
        this.competitionTier = competitionTier;
        this.trendDirection = trendDirection;
        this.beginnerAccessibility = beginnerAccessibility;
        this.active = active;
        this.logoUrl = logoUrl;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return shortName;
    }

    public String getCode() {
        return code;
    }

    public String getLeague() {
        return league;
    }

    public String getCountry() {
        return country;
    }

    public String getCompetitionTier() {
        return competitionTier;
    }

    public String getTrendDirection() {
        return trendDirection;
    }

    public BigDecimal getBeginnerAccessibility() {
        return beginnerAccessibility;
    }

    public Boolean isActive() {
        return active;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
