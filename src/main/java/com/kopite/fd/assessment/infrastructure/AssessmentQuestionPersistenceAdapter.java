package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.AssessmentQuestion;
import com.kopite.fd.assessment.domain.AssessmentQuestionRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentQuestionPersistenceAdapter implements AssessmentQuestionRepository {

    private final AssessmentQuestionJpaRepository assessmentQuestionJpaRepository;

    public AssessmentQuestionPersistenceAdapter(AssessmentQuestionJpaRepository assessmentQuestionJpaRepository) {
        this.assessmentQuestionJpaRepository = assessmentQuestionJpaRepository;
    }

    @Override
    public List<AssessmentQuestion> findActiveByQuestionVersion(String questionVersion) {
        return assessmentQuestionJpaRepository.findByQuestionVersionAndActiveTrueOrderByDisplayOrderAsc(questionVersion)
                .stream()
                .map(AssessmentQuestionJpaEntity::toDomain)
                .toList();
    }
}
