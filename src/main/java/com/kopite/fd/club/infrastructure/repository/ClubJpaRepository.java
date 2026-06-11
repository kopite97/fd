package com.kopite.fd.club.infrastructure.repository;

import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubJpaRepository extends JpaRepository<ClubJpaEntity, Long> {

    Optional<ClubJpaEntity> findByIdAndIsDeletedFalse(Long id);

    Optional<ClubJpaEntity> findByCodeAndIsDeletedFalse(String code);
}
