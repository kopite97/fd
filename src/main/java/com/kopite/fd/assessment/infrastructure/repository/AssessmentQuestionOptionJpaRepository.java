package com.kopite.fd.assessment.infrastructure.repository;

import com.kopite.fd.assessment.infrastructure.entity.AssessmentQuestionOptionJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentQuestionOptionJpaRepository extends JpaRepository<AssessmentQuestionOptionJpaEntity, Long> {

    List<AssessmentQuestionOptionJpaEntity> findByQuestionIdInAndActiveTrueOrderByQuestionIdAscDisplayOrderAsc(List<Long> questionIds);
}
