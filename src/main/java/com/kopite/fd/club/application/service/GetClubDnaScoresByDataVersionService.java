package com.kopite.fd.club.application.service;

import com.kopite.fd.club.application.query.GetClubDnaScoresByDataVersionQuery;
import com.kopite.fd.club.application.result.ClubDnaScoreResult;
import com.kopite.fd.club.domain.model.ClubDnaScore;
import com.kopite.fd.club.domain.repository.ClubDnaScoreRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetClubDnaScoresByDataVersionService {

    private final ClubDnaScoreRepository clubDnaScoreRepository;

    public List<ClubDnaScoreResult> getClubDnaScoresByDataVersion(GetClubDnaScoresByDataVersionQuery query) {
        String dataVersion = normalizeRequired(query.dataVersion(), "dataVersion");

        return clubDnaScoreRepository.findByDataVersion(dataVersion).stream()
                .map(this::toResult)
                .toList();
    }

    private ClubDnaScoreResult toResult(ClubDnaScore clubDnaScore) {
        return new ClubDnaScoreResult(
                clubDnaScore.getId(),
                clubDnaScore.getClubId(),
                clubDnaScore.getDnaDefinitionId(),
                clubDnaScore.getDnaCategory(),
                clubDnaScore.getDnaKey(),
                clubDnaScore.getDnaDisplayName(),
                clubDnaScore.getDnaDisplayOrder(),
                clubDnaScore.getScore(),
                clubDnaScore.isCore(),
                clubDnaScore.getDataVersion(),
                clubDnaScore.getCreatedAt(),
                clubDnaScore.getUpdatedAt()
        );
    }

    private String normalizeRequired(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
        }
        return value.trim();
    }
}
