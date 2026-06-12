package com.kopite.fd.club.infrastructure.adapter;

import com.kopite.fd.club.domain.model.ClubTag;
import com.kopite.fd.club.domain.repository.ClubTagRepository;
import com.kopite.fd.club.infrastructure.entity.ClubTagJpaEntity;
import com.kopite.fd.club.infrastructure.repository.ClubTagJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClubTagPersistenceAdapter implements ClubTagRepository {

    private final ClubTagJpaRepository clubTagJpaRepository;

    @Override
    public List<ClubTag> findActiveByClubId(Long clubId) {
        return clubTagJpaRepository.findByClubIdAndActiveTrueAndIsDeletedFalseOrderByDisplayOrderAscIdAsc(clubId)
                .stream()
                .map(ClubTagJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<ClubTag> findAllActive() {
        return clubTagJpaRepository.findByActiveTrueAndIsDeletedFalseOrderByClubIdAscDisplayOrderAscIdAsc()
                .stream()
                .map(ClubTagJpaEntity::toDomain)
                .toList();
    }
}
