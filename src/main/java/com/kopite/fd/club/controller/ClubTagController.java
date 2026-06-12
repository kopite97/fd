package com.kopite.fd.club.controller;

import com.kopite.fd.club.application.query.GetClubTagsByClubIdQuery;
import com.kopite.fd.club.application.result.ClubTagResult;
import com.kopite.fd.club.application.service.GetAllClubTagsService;
import com.kopite.fd.club.application.service.GetClubTagsByClubIdService;
import com.kopite.fd.club.dto.response.ClubTagsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Club Tags", description = "Club tag read APIs")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClubTagController {

    private final GetClubTagsByClubIdService getClubTagsByClubIdService;
    private final GetAllClubTagsService getAllClubTagsService;

    @Operation(summary = "Get tags for a club", description = "Returns active, non-deleted tags for the club.")
    @GetMapping("/clubs/{clubId}/tags")
    public ClubTagsResponse getClubTagsByClubId(
            @Parameter(description = "Club identifier", required = true)
            @PathVariable Long clubId
    ) {
        List<ClubTagResult> results = getClubTagsByClubIdService.getClubTagsByClubId(
                new GetClubTagsByClubIdQuery(clubId)
        );
        return ClubTagsResponse.from(results);
    }

    @Operation(summary = "Get all club tags", description = "Returns all active, non-deleted club tags.")
    @GetMapping("/club-tags")
    public ClubTagsResponse getAllClubTags() {
        return ClubTagsResponse.from(getAllClubTagsService.getAllClubTags());
    }
}
