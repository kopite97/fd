package com.kopite.fd.club.dto.response;

import com.kopite.fd.club.application.result.ClubPageResult;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Club list response")
public record ClubListResponse(
        @Schema(description = "Club summaries")
        List<ClubSummaryResponse> clubs,

        @Schema(description = "Current page number", example = "0")
        int page,

        @Schema(description = "Page size", example = "20")
        int size,

        @Schema(description = "Total matching clubs", example = "20")
        long totalElements,

        @Schema(description = "Total pages", example = "1")
        int totalPages
) {

    public static ClubListResponse from(ClubPageResult result) {
        return new ClubListResponse(
                result.clubs().stream()
                        .map(ClubSummaryResponse::from)
                        .toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }
}
