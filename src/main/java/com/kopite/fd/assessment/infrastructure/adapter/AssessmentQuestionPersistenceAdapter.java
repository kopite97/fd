package com.kopite.fd.assessment.infrastructure.adapter;

import com.kopite.fd.assessment.domain.model.AssessmentQuestion;
import com.kopite.fd.assessment.domain.repository.AssessmentQuestionRepository;
import com.kopite.fd.assessment.infrastructure.entity.AssessmentQuestionJpaEntity;
import com.kopite.fd.assessment.infrastructure.repository.AssessmentQuestionJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AssessmentQuestionPersistenceAdapter implements AssessmentQuestionRepository {

    private final AssessmentQuestionJpaRepository assessmentQuestionJpaRepository;

    @Override
    public List<AssessmentQuestion> findActiveByQuestionVersion(String questionVersion) {
        return assessmentQuestionJpaRepository.findByQuestionVersionAndActiveTrueOrderByDisplayOrderAsc(
                        Integer.valueOf(questionVersion)
                )
                .stream()
                .map(AssessmentQuestionJpaEntity::toDomain)
                .toList();
    }
}
