package com.kopite.fd.assessment.infrastructure.repository;

import com.kopite.fd.assessment.infrastructure.entity.OptionScoreMappingJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionScoreMappingJpaRepository extends JpaRepository<OptionScoreMappingJpaEntity, Long> {

    List<OptionScoreMappingJpaEntity> findByOptionIdInAndIsDeletedFalse(List<Long> optionIds);
}
