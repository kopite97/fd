package com.kopite.fd.assessment.infrastructure.repository;

import com.kopite.fd.assessment.infrastructure.entity.AssessmentQuestionJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentQuestionJpaRepository extends JpaRepository<AssessmentQuestionJpaEntity, Long> {

    List<AssessmentQuestionJpaEntity> findByQuestionVersionAndActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc(
            String questionVersion
    );
}
