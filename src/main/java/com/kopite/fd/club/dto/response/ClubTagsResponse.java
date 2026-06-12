package com.kopite.fd.club.dto.response;

import com.kopite.fd.club.application.result.ClubTagResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Club tags response")
public record ClubTagsResponse(
        @Schema(description = "Club tags")
        List<ClubTagResponse> tags
) {

    public static ClubTagsResponse from(List<ClubTagResult> results) {
        return new ClubTagsResponse(results.stream()
                .map(ClubTagResponse::from)
                .toList());
    }
}
