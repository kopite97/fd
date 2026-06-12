package com.kopite.fd.club.application.service;

import com.kopite.fd.club.application.query.GetPublicClubsQuery;
import com.kopite.fd.club.application.result.ClubPageResult;
import com.kopite.fd.club.application.result.ClubResult;
import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.domain.repository.ClubRepository;
import com.kopite.fd.club.domain.repository.result.ClubPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetPublicClubsService {

    private final ClubRepository clubRepository;

    public ClubPageResult getPublicClubs(GetPublicClubsQuery query) {
        if (query.page() < 0) {
            throw new IllegalArgumentException("page must not be negative.");
        }
        if (query.size() < 1) {
            throw new IllegalArgumentException("size must be positive.");
        }
        ClubPage clubPage = clubRepository.findPublicClubs(query.page(), query.size());
        return new ClubPageResult(
                clubPage.clubs().stream()
                        .map(this::toResult)
                        .toList(),
                clubPage.page(),
                clubPage.size(),
                clubPage.totalElements(),
                clubPage.totalPages()
        );
    }

    private ClubResult toResult(Club club) {
        return new ClubResult(
                club.getId(),
                club.getName(),
                club.getShortName(),
                club.getCode(),
                club.getLeague(),
                club.getCountry(),
                club.getCompetitionTier(),
                club.getTrendDirection(),
                club.getBeginnerAccessibility(),
                club.isActive(),
                club.getLogoUrl(),
                club.getPrimaryColor(),
                club.getSecondaryColor(),
                club.getCreatedAt(),
                club.getUpdatedAt()
        );
    }
}
