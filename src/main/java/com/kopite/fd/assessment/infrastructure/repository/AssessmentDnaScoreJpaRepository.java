package com.kopite.fd.assessment.infrastructure.repository;

import com.kopite.fd.assessment.infrastructure.entity.AssessmentDnaScoreJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentDnaScoreJpaRepository extends JpaRepository<AssessmentDnaScoreJpaEntity, Long> {

    List<AssessmentDnaScoreJpaEntity> findByAssessmentId(Long assessmentId);
}
