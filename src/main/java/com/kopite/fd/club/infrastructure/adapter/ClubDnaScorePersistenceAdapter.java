package com.kopite.fd.club.infrastructure.adapter;

import com.kopite.fd.club.domain.model.ClubDnaScore;
import com.kopite.fd.club.domain.repository.ClubDnaScoreRepository;
import com.kopite.fd.club.infrastructure.repository.ClubDnaScoreJpaRepository;
import com.kopite.fd.club.infrastructure.repository.ClubDnaScoreProjection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClubDnaScorePersistenceAdapter implements ClubDnaScoreRepository {

    private final ClubDnaScoreJpaRepository clubDnaScoreJpaRepository;

    @Override
    public List<ClubDnaScore> findByClubIdAndDataVersion(Long clubId, String dataVersion) {
        return clubDnaScoreJpaRepository.findProjectedByClubIdAndDataVersion(clubId, dataVersion).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<ClubDnaScore> findByDataVersion(String dataVersion) {
        return clubDnaScoreJpaRepository.findProjectedByDataVersion(dataVersion).stream()
                .map(this::toDomain)
                .toList();
    }

    private ClubDnaScore toDomain(ClubDnaScoreProjection projection) {
        return new ClubDnaScore(
                projection.getId(),
                projection.getClubId(),
                projection.getDnaDefinitionId(),
                projection.getScore(),
                projection.getCore(),
                projection.getDataVersion(),
                projection.getDnaCategory(),
                projection.getDnaKey(),
                projection.getDnaDisplayName(),
                projection.getDnaDisplayOrder(),
                projection.getCreatedAt(),
                projection.getUpdatedAt()
        );
    }
}
