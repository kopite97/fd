package com.kopite.fd.assessment.infrastructure.adapter;

import com.kopite.fd.assessment.domain.model.Assessment;
import com.kopite.fd.assessment.domain.repository.AssessmentRepository;
import com.kopite.fd.assessment.infrastructure.entity.AssessmentJpaEntity;
import com.kopite.fd.assessment.infrastructure.repository.AssessmentJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AssessmentPersistenceAdapter implements AssessmentRepository {

    private final AssessmentJpaRepository assessmentJpaRepository;

    @Override
    public Assessment save(Assessment assessment) {
        AssessmentJpaEntity assessmentJpaEntity = AssessmentJpaEntity.fromDomain(assessment);
        return assessmentJpaRepository.save(assessmentJpaEntity).toDomain();
    }

    @Override
    public Optional<Assessment> findById(Long assessmentId) {
        return assessmentJpaRepository.findByIdAndIsDeletedFalse(assessmentId)
                .map(AssessmentJpaEntity::toDomain);
    }
}
