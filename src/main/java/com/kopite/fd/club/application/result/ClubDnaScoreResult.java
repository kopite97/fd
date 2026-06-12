package com.kopite.fd.club.application.result;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ClubDnaScoreResult(
        Long id,
        Long clubId,
        Long dnaDefinitionId,
        String dnaCategory,
        String dnaKey,
        String dnaDisplayName,
        Integer dnaDisplayOrder,
        BigDecimal score,
        Boolean core,
        String dataVersion,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
