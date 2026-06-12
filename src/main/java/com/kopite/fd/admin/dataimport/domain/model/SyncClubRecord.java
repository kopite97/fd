package com.kopite.fd.admin.dataimport.domain.model;

import java.math.BigDecimal;

public record SyncClubRecord(
        String code,
        String name,
        String shortName,
        String league,
        String country,
        String competitionTier,
        String trendDirection,
        BigDecimal beginnerAccessibility,
        Boolean active
) {
}
