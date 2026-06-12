package com.kopite.fd.club.infrastructure.repository;

import com.kopite.fd.club.infrastructure.entity.ClubTagJpaEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubTagJpaRepository extends JpaRepository<ClubTagJpaEntity, Long> {

    List<ClubTagJpaEntity> findByClubIdAndActiveTrueAndIsDeletedFalseOrderByDisplayOrderAscIdAsc(Long clubId);

    List<ClubTagJpaEntity> findByClubIdIn(Collection<Long> clubIds);

    List<ClubTagJpaEntity> findByActiveTrueAndIsDeletedFalseOrderByClubIdAscDisplayOrderAscIdAsc();
}
