package com.kopite.fd.club.application.command;

import java.math.BigDecimal;

public record SaveClubCommand(
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
        String secondaryColor
) {
}
