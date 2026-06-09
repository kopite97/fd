package com.kopite.fd.assessment.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentQuestionOptionJpaRepository extends JpaRepository<AssessmentQuestionOptionJpaEntity, Long> {

    List<AssessmentQuestionOptionJpaEntity> findByQuestionIdInAndActiveTrueOrderByQuestionIdAscDisplayOrderAsc(List<Long> questionIds);
}
