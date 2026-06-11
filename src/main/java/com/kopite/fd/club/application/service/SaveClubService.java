package com.kopite.fd.club.application.service;

import com.kopite.fd.club.application.command.SaveClubCommand;
import com.kopite.fd.club.application.result.ClubResult;
import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.domain.repository.ClubRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SaveClubService {

    private static final BigDecimal DEFAULT_BEGINNER_ACCESSIBILITY = BigDecimal.ZERO;
    private static final Boolean DEFAULT_ACTIVE = true;

    private final ClubRepository clubRepository;

    public ClubResult saveClub(SaveClubCommand command) {
        String normalizedCode = normalizeRequired(command.code(), "code");
        LocalDateTime now = LocalDateTime.now();
        Club existingClub = command.id() != null
                ? clubRepository.findById(command.id()).orElse(null)
                : clubRepository.findByCode(normalizedCode).orElse(null);

        Club savedClub = clubRepository.save(new Club(
                existingClub != null ? existingClub.getId() : command.id(),
                normalizeRequired(command.name(), "name"),
                normalizeRequired(command.shortName(), "shortName"),
                normalizedCode,
                normalizeRequired(command.league(), "league"),
                normalizeRequired(command.country(), "country"),
                normalizeRequired(command.competitionTier(), "competitionTier"),
                normalizeRequired(command.trendDirection(), "trendDirection"),
                command.beginnerAccessibility() == null ? DEFAULT_BEGINNER_ACCESSIBILITY : command.beginnerAccessibility(),
                command.active() == null ? DEFAULT_ACTIVE : command.active(),
                normalizeOptional(command.logoUrl()),
                normalizeOptional(command.primaryColor()),
                normalizeOptional(command.secondaryColor()),
                existingClub != null ? existingClub.getCreatedAt() : now,
                now
        ));
        return new ClubResult(
                savedClub.getId(),
                savedClub.getName(),
                savedClub.getShortName(),
                savedClub.getCode(),
                savedClub.getLeague(),
                savedClub.getCountry(),
                savedClub.getCompetitionTier(),
                savedClub.getTrendDirection(),
                savedClub.getBeginnerAccessibility(),
                savedClub.isActive(),
                savedClub.getLogoUrl(),
                savedClub.getPrimaryColor(),
                savedClub.getSecondaryColor(),
                savedClub.getCreatedAt(),
                savedClub.getUpdatedAt()
        );
    }

    private String normalizeRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.trim();
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
