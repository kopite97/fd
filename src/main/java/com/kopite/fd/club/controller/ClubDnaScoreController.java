package com.kopite.fd.club.controller;

import com.kopite.fd.club.application.query.GetClubDnaScoresByClubIdQuery;
import com.kopite.fd.club.application.query.GetClubDnaScoresByDataVersionQuery;
import com.kopite.fd.club.application.result.ClubDnaScoreResult;
import com.kopite.fd.club.application.service.GetClubDnaScoresByClubIdService;
import com.kopite.fd.club.application.service.GetClubDnaScoresByDataVersionService;
import com.kopite.fd.club.dto.response.ClubDnaScoresResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Club DNA Scores", description = "Club DNA score read APIs")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClubDnaScoreController {

    private final GetClubDnaScoresByClubIdService getClubDnaScoresByClubIdService;
    private final GetClubDnaScoresByDataVersionService getClubDnaScoresByDataVersionService;

    @Operation(
            summary = "Get DNA scores for a club",
            description = "Returns non-deleted club DNA scores for the exact club data version."
    )
    @GetMapping("/clubs/{clubId}/dna-scores")
    public ClubDnaScoresResponse getClubDnaScoresByClubId(
            @Parameter(description = "Club identifier", required = true)
            @PathVariable Long clubId,
            @Parameter(description = "Club data snapshot version", required = true, example = "club-v1")
            @RequestParam String dataVersion
    ) {
        List<ClubDnaScoreResult> results = getClubDnaScoresByClubIdService.getClubDnaScoresByClubId(
                new GetClubDnaScoresByClubIdQuery(clubId, dataVersion)
        );
        return ClubDnaScoresResponse.from(results);
    }

    @Operation(
            summary = "Get all club DNA scores",
            description = "Returns all non-deleted club DNA scores for the exact club data version."
    )
    @GetMapping("/club-dna-scores")
    public ClubDnaScoresResponse getClubDnaScoresByDataVersion(
            @Parameter(description = "Club data snapshot version", required = true, example = "club-v1")
            @RequestParam String dataVersion
    ) {
        return ClubDnaScoresResponse.from(getClubDnaScoresByDataVersionService.getClubDnaScoresByDataVersion(
                new GetClubDnaScoresByDataVersionQuery(dataVersion)
        ));
    }
}
