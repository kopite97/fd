package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.Assessment;
import com.kopite.fd.assessment.domain.AssessmentRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentPersistenceAdapter implements AssessmentRepository {

    private final AssessmentJpaRepository assessmentJpaRepository;

    public AssessmentPersistenceAdapter(AssessmentJpaRepository assessmentJpaRepository) {
        this.assessmentJpaRepository = assessmentJpaRepository;
    }

    @Override
    public Assessment save(Assessment assessment) {
        AssessmentJpaEntity assessmentJpaEntity = AssessmentJpaEntity.fromDomain(assessment);
        return assessmentJpaRepository.save(assessmentJpaEntity).toDomain();
    }

    @Override
    public Optional<Assessment> findById(Long assessmentId) {
        return assessmentJpaRepository.findById(assessmentId)
                .map(AssessmentJpaEntity::toDomain);
    }
}
