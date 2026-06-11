package com.kopite.fd.assessment.infrastructure.repository;

import com.kopite.fd.assessment.infrastructure.entity.AssessmentJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentJpaRepository extends JpaRepository<AssessmentJpaEntity, Long> {

    Optional<AssessmentJpaEntity> findByIdAndIsDeletedFalse(Long id);
}
