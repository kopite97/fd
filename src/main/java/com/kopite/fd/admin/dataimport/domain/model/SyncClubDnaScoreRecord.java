package com.kopite.fd.admin.dataimport.domain.model;

import java.math.BigDecimal;

public record SyncClubDnaScoreRecord(
        String clubCode,
        String dnaCategory,
        String dnaKey,
        BigDecimal score,
        Boolean core,
        String dataVersion
) {
}
