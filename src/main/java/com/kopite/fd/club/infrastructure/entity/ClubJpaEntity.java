package com.kopite.fd.club.infrastructure.entity;

import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "clubs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "short_name", nullable = false, length = 30)
    private String shortName;

    @Column(name = "code", nullable = false, length = 20, unique = true)
    private String code;

    @Column(name = "league", nullable = false, length = 50)
    private String league;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "competition_tier", nullable = false, length = 30)
    private String competitionTier;

    @Column(name = "trend_direction", nullable = false, length = 30)
    private String trendDirection;

    @Column(name = "beginner_accessibility", nullable = false, precision = 5, scale = 2)
    private BigDecimal beginnerAccessibility;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @Column(name = "logo_url", nullable = true, length = 500)
    private String logoUrl;

    @Column(name = "primary_color", nullable = true, length = 7)
    private String primaryColor;

    @Column(name = "secondary_color", nullable = true, length = 7)
    private String secondaryColor;

    private ClubJpaEntity(
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
        super(createdAt, updatedAt, false);
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
    }

    public static ClubJpaEntity fromDomain(Club club) {
        return new ClubJpaEntity(
                club.getId(),
                club.getName(),
                club.getShortName(),
                club.getCode(),
                club.getLeague(),
                club.getCountry(),
                club.getCompetitionTier(),
                club.getTrendDirection(),
                club.getBeginnerAccessibility(),
                club.isActive(),
                club.getLogoUrl(),
                club.getPrimaryColor(),
                club.getSecondaryColor(),
                club.getCreatedAt(),
                club.getUpdatedAt()
        );
    }

    public static ClubJpaEntity createForSync(
            String name,
            String shortName,
            String code,
            String league,
            String country,
            String competitionTier,
            String trendDirection,
            BigDecimal beginnerAccessibility,
            Boolean active
    ) {
        LocalDateTime now = LocalDateTime.now();
        return new ClubJpaEntity(
                null,
                name,
                shortName,
                code,
                league,
                country,
                competitionTier,
                trendDirection,
                beginnerAccessibility,
                active,
                null,
                null,
                null,
                now,
                now
        );
    }

    public void updateFromSync(
            String name,
            String shortName,
            String league,
            String country,
            String competitionTier,
            String trendDirection,
            BigDecimal beginnerAccessibility,
            Boolean active
    ) {
        this.name = name;
        this.shortName = shortName;
        this.league = league;
        this.country = country;
        this.competitionTier = competitionTier;
        this.trendDirection = trendDirection;
        this.beginnerAccessibility = beginnerAccessibility;
        this.active = active;
        restore();
    }

    public void markSoftDeleted() {
        markDeleted();
    }

    public Club toDomain() {
        return new Club(
                id,
                name,
                shortName,
                code,
                league,
                country,
                competitionTier,
                trendDirection,
                beginnerAccessibility,
                active,
                logoUrl,
                primaryColor,
                secondaryColor,
                getCreatedAt(),
                getUpdatedAt()
        );
    }
}
