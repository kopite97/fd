package com.kopite.fd.assessment.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentQuestionJpaRepository extends JpaRepository<AssessmentQuestionJpaEntity, Long> {

    List<AssessmentQuestionJpaEntity> findByQuestionVersionAndActiveTrueOrderByDisplayOrderAsc(String questionVersion);
}
