package com.kopite.fd.club.dto.response;

import com.kopite.fd.club.application.result.ClubResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Club detail response")
public record ClubDetailResponse(
        @Schema(description = "Club identifier", example = "1")
        Long clubId,

        @Schema(description = "Official club name", example = "Arsenal")
        String name,

        @Schema(description = "Short club name", example = "AFC")
        String shortName,

        @Schema(description = "Club code", example = "ARS")
        String code,

        @Schema(description = "League name", example = "EPL")
        String league,

        @Schema(description = "Country", example = "England")
        String country,

        @Schema(description = "Competitive tier", example = "TITLE_CONTENDER")
        String competitionTier,

        @Schema(description = "Trend direction", example = "RISING")
        String trendDirection,

        @Schema(description = "Beginner accessibility", example = "4.50")
        BigDecimal beginnerAccessibility,

        @Schema(description = "Logo URL", nullable = true)
        String logoUrl,

        @Schema(description = "Primary HEX color", example = "#EF0107", nullable = true)
        String primaryColor,

        @Schema(description = "Secondary HEX color", example = "#FFFFFF", nullable = true)
        String secondaryColor
) {

    public static ClubDetailResponse from(ClubResult result) {
        return new ClubDetailResponse(
                result.id(),
                result.name(),
                result.shortName(),
                result.code(),
                result.league(),
                result.country(),
                result.competitionTier(),
                result.trendDirection(),
                result.beginnerAccessibility(),
                result.logoUrl(),
                result.primaryColor(),
                result.secondaryColor()
        );
    }
}
