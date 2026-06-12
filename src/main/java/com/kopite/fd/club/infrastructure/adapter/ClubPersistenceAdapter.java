package com.kopite.fd.club.infrastructure.adapter;

import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.domain.repository.ClubRepository;
import com.kopite.fd.club.domain.repository.result.ClubPage;
import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import com.kopite.fd.club.infrastructure.repository.ClubJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    @Override
    public ClubPage findPublicClubs(int page, int size) {
        Page<ClubJpaEntity> clubPage = clubJpaRepository.findByActiveTrueAndIsDeletedFalse(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"))
        );
        return new ClubPage(
                clubPage.getContent().stream()
                        .map(ClubJpaEntity::toDomain)
                        .toList(),
                clubPage.getNumber(),
                clubPage.getSize(),
                clubPage.getTotalElements(),
                clubPage.getTotalPages()
        );
    }

    @Override
    public Optional<Club> findPublicClubById(Long clubId) {
        return clubJpaRepository.findByIdAndActiveTrueAndIsDeletedFalse(clubId)
                .map(ClubJpaEntity::toDomain);
    }
}
