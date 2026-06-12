package com.kopite.fd.club.controller;

import com.kopite.fd.club.application.query.GetPublicClubByIdQuery;
import com.kopite.fd.club.application.query.GetPublicClubsQuery;
import com.kopite.fd.club.application.service.GetPublicClubByIdService;
import com.kopite.fd.club.application.service.GetPublicClubsService;
import com.kopite.fd.club.dto.response.ClubDetailResponse;
import com.kopite.fd.club.dto.response.ClubListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Clubs", description = "Public club read APIs")
@RestController
@RequestMapping("/api/clubs")
@RequiredArgsConstructor
public class ClubController {

    private final GetPublicClubsService getPublicClubsService;
    private final GetPublicClubByIdService getPublicClubByIdService;

    @Operation(
            summary = "Get public clubs",
            description = "Returns active, non-deleted clubs ordered by club ID."
    )
    @GetMapping
    public ClubListResponse getPublicClubs(
            @Parameter(description = "Page number", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "20")
            @RequestParam(defaultValue = "20") int size
    ) {
        return ClubListResponse.from(getPublicClubsService.getPublicClubs(new GetPublicClubsQuery(page, size)));
    }

    @Operation(
            summary = "Get public club detail",
            description = "Returns one active, non-deleted club by identifier."
    )
    @GetMapping("/{clubId}")
    @ResponseStatus(HttpStatus.OK)
    public ClubDetailResponse getPublicClubById(
            @Parameter(description = "Club identifier", required = true)
            @PathVariable Long clubId
    ) {
        return getPublicClubByIdService.getPublicClubById(new GetPublicClubByIdQuery(clubId))
                .map(ClubDetailResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Club not found."));
    }
}
