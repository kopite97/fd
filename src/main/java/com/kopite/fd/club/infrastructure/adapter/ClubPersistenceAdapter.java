package com.kopite.fd.club.infrastructure.adapter;

import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.domain.repository.ClubRepository;
import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import com.kopite.fd.club.infrastructure.repository.ClubJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ClubPersistenceAdapter implements ClubRepository {

    private final ClubJpaRepository clubJpaRepository;

    @Override
    public Club save(Club club) {
        return clubJpaRepository.save(ClubJpaEntity.fromDomain(club)).toDomain();
    }

    @Override
    public Optional<Club> findById(Long clubId) {
        return clubJpaRepository.findByIdAndIsDeletedFalse(clubId)
                .map(ClubJpaEntity::toDomain);
    }

    @Override
    public Optional<Club> findByCode(String code) {
        return clubJpaRepository.findByCodeAndIsDeletedFalse(code)
                .map(ClubJpaEntity::toDomain);
    }
}
