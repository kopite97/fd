package com.kopite.fd.assessment.infrastructure.repository;

import com.kopite.fd.assessment.infrastructure.entity.AssessmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentJpaRepository extends JpaRepository<AssessmentJpaEntity, Long> {
}
