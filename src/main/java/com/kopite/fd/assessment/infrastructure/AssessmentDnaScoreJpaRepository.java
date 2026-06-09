package com.kopite.fd.assessment.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentDnaScoreJpaRepository extends JpaRepository<AssessmentDnaScoreJpaEntity, Long> {

    List<AssessmentDnaScoreJpaEntity> findByAssessmentId(Long assessmentId);
}
