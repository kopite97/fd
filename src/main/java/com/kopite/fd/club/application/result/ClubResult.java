package com.kopite.fd.club.application.result;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClubResult(
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
}
