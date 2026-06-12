package com.kopite.fd.club.dto.response;

import com.kopite.fd.club.application.result.ClubDnaScoreResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Club DNA scores response")
public record ClubDnaScoresResponse(
        @Schema(description = "Club DNA scores")
        List<ClubDnaScoreResponse> scores
) {

    public static ClubDnaScoresResponse from(List<ClubDnaScoreResult> results) {
        return new ClubDnaScoresResponse(results.stream()
                .map(ClubDnaScoreResponse::from)
                .toList());
    }
}
