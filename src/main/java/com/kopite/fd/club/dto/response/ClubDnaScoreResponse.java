package com.kopite.fd.club.dto.response;

import com.kopite.fd.club.application.result.ClubDnaScoreResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Club DNA score response")
public record ClubDnaScoreResponse(
        @Schema(description = "Club DNA score identifier", example = "1")
        Long scoreId,

        @Schema(description = "Club identifier", example = "10")
        Long clubId,

        @Schema(description = "DNA definition identifier", example = "100")
        Long dnaDefinitionId,

        @Schema(description = "DNA category from dna_definitions", example = "EMOTIONAL")
        String dnaCategory,

        @Schema(description = "DNA key from dna_definitions", example = "fan_culture")
        String dnaKey,

        @Schema(description = "DNA display name from dna_definitions", example = "Fan Culture")
        String dnaDisplayName,

        @Schema(description = "DNA display order", example = "1")
        Integer dnaDisplayOrder,

        @Schema(description = "Club DNA score on the 1.00 to 5.00 scale", example = "4.50")
        BigDecimal score,

        @Schema(description = "Whether this DNA is a core club DNA", example = "true")
        Boolean core,

        @Schema(description = "Club data snapshot version", example = "club-v1")
        String dataVersion
) {

    public static ClubDnaScoreResponse from(ClubDnaScoreResult result) {
        return new ClubDnaScoreResponse(
                result.id(),
                result.clubId(),
                result.dnaDefinitionId(),
                result.dnaCategory(),
                result.dnaKey(),
                result.dnaDisplayName(),
                result.dnaDisplayOrder(),
                result.score(),
                result.core(),
                result.dataVersion()
        );
    }
}
