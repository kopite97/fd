package com.kopite.fd.club.application.service;

import com.kopite.fd.club.application.query.GetClubByCodeQuery;
import com.kopite.fd.club.application.result.ClubResult;
import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.domain.repository.ClubRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetClubByCodeService {

    private final ClubRepository clubRepository;

    public Optional<ClubResult> getClubByCode(GetClubByCodeQuery query) {
        String code = normalizeRequired(query.code(), "code");
        return clubRepository.findByCode(code)
                .map(this::toResult);
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

    private String normalizeRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.trim();
    }
}
