package com.kopite.fd.assessment.infrastructure.repository;

import com.kopite.fd.assessment.infrastructure.entity.AssessmentAnswerJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentAnswerJpaRepository extends JpaRepository<AssessmentAnswerJpaEntity, Long> {

    List<AssessmentAnswerJpaEntity> findByAssessmentId(Long assessmentId);
}
