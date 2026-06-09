package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.AssessmentAnswer;
import com.kopite.fd.assessment.domain.AssessmentAnswerRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentAnswerPersistenceAdapter implements AssessmentAnswerRepository {

    private final AssessmentAnswerJpaRepository assessmentAnswerJpaRepository;

    public AssessmentAnswerPersistenceAdapter(AssessmentAnswerJpaRepository assessmentAnswerJpaRepository) {
        this.assessmentAnswerJpaRepository = assessmentAnswerJpaRepository;
    }

    @Override
    public AssessmentAnswer save(AssessmentAnswer assessmentAnswer) {
        AssessmentAnswerJpaEntity assessmentAnswerJpaEntity = AssessmentAnswerJpaEntity.fromDomain(assessmentAnswer);
        return assessmentAnswerJpaRepository.save(assessmentAnswerJpaEntity).toDomain();
    }

    @Override
    public List<AssessmentAnswer> findByAssessmentId(Long assessmentId) {
        return assessmentAnswerJpaRepository.findByAssessmentId(assessmentId).stream()
                .map(AssessmentAnswerJpaEntity::toDomain)
                .toList();
    }
}
