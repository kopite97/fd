package com.kopite.fd.assessment.infrastructure.adapter;

import com.kopite.fd.assessment.domain.model.AssessmentQuestionOption;
import com.kopite.fd.assessment.domain.repository.AssessmentQuestionOptionRepository;
import com.kopite.fd.assessment.infrastructure.entity.AssessmentQuestionOptionJpaEntity;
import com.kopite.fd.assessment.infrastructure.repository.AssessmentQuestionOptionJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AssessmentQuestionOptionPersistenceAdapter implements AssessmentQuestionOptionRepository {

    private final AssessmentQuestionOptionJpaRepository assessmentQuestionOptionJpaRepository;

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
