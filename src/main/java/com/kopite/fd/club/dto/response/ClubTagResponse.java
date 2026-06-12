package com.kopite.fd.club.dto.response;

import com.kopite.fd.club.application.result.ClubTagResult;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Club tag response")
public record ClubTagResponse(
        @Schema(description = "Club tag identifier", example = "1")
        Long tagId,

        @Schema(description = "Club identifier", example = "10")
        Long clubId,

        @Schema(description = "Tag name without presentation-only # prefix", example = "High pressing")
        String tagName,

        @Schema(description = "Tag category", example = "STYLE", nullable = true)
        String tagType,

        @Schema(description = "Display order", example = "1")
        Integer displayOrder
) {

    public static ClubTagResponse from(ClubTagResult result) {
        return new ClubTagResponse(
                result.id(),
                result.clubId(),
                result.tagName(),
                result.tagType(),
                result.displayOrder()
        );
    }
}
