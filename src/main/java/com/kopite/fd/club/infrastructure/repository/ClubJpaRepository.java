package com.kopite.fd.club.infrastructure.repository;

import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubJpaRepository extends JpaRepository<ClubJpaEntity, Long> {

    Optional<ClubJpaEntity> findByIdAndIsDeletedFalse(Long id);

    Optional<ClubJpaEntity> findByCodeAndIsDeletedFalse(String code);

    Optional<ClubJpaEntity> findByCode(String code);

    List<ClubJpaEntity> findByCodeIn(Collection<String> codes);

    List<ClubJpaEntity> findByIsDeletedFalse();

    Page<ClubJpaEntity> findByActiveTrueAndIsDeletedFalse(Pageable pageable);

    Optional<ClubJpaEntity> findByIdAndActiveTrueAndIsDeletedFalse(Long id);
}
