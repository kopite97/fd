package com.kopite.fd.club.dto.response;

import com.kopite.fd.club.application.result.ClubResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Club summary response")
public record ClubSummaryResponse(
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

        @Schema(description = "Logo URL", nullable = true)
        String logoUrl,

        @Schema(description = "Primary HEX color", example = "#EF0107", nullable = true)
        String primaryColor,

        @Schema(description = "Secondary HEX color", example = "#FFFFFF", nullable = true)
        String secondaryColor
) {

    public static ClubSummaryResponse from(ClubResult result) {
        return new ClubSummaryResponse(
                result.id(),
                result.name(),
                result.shortName(),
                result.code(),
                result.league(),
                result.country(),
                result.logoUrl(),
                result.primaryColor(),
                result.secondaryColor()
        );
    }
}
