package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.AssessmentQuestionOption;
import com.kopite.fd.assessment.domain.AssessmentQuestionOptionRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentQuestionOptionPersistenceAdapter implements AssessmentQuestionOptionRepository {

    private final AssessmentQuestionOptionJpaRepository assessmentQuestionOptionJpaRepository;

    public AssessmentQuestionOptionPersistenceAdapter(AssessmentQuestionOptionJpaRepository assessmentQuestionOptionJpaRepository) {
        this.assessmentQuestionOptionJpaRepository = assessmentQuestionOptionJpaRepository;
    }

    @Override
    public List<AssessmentQuestionOption> findActiveByQuestionIds(List<Long> questionIds) {
        if (questionIds.isEmpty()) {
            return List.of();
        }

        return assessmentQuestionOptionJpaRepository.findByQuestionIdInAndActiveTrueOrderByQuestionIdAscDisplayOrderAsc(questionIds)
                .stream()
                .map(AssessmentQuestionOptionJpaEntity::toDomain)
                .toList();
    }
}
