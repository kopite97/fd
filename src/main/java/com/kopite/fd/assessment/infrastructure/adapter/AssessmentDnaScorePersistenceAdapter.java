package com.kopite.fd.assessment.infrastructure.adapter;

import com.kopite.fd.assessment.domain.model.AssessmentDnaScore;
import com.kopite.fd.assessment.domain.repository.AssessmentDnaScoreRepository;
import com.kopite.fd.assessment.infrastructure.entity.AssessmentDnaScoreJpaEntity;
import com.kopite.fd.assessment.infrastructure.repository.AssessmentDnaScoreJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AssessmentDnaScorePersistenceAdapter implements AssessmentDnaScoreRepository {

    private final AssessmentDnaScoreJpaRepository assessmentDnaScoreJpaRepository;

    @Override
    public List<AssessmentDnaScore> saveAll(List<AssessmentDnaScore> assessmentDnaScores) {
        List<AssessmentDnaScoreJpaEntity> assessmentDnaScoreJpaEntities = assessmentDnaScores.stream()
                .map(AssessmentDnaScoreJpaEntity::fromDomain)
                .toList();

        return assessmentDnaScoreJpaRepository.saveAll(assessmentDnaScoreJpaEntities).stream()
                .map(AssessmentDnaScoreJpaEntity::toDomain)
                .toList();
    }

    @Override
    public List<AssessmentDnaScore> findByAssessmentId(Long assessmentId) {
        return assessmentDnaScoreJpaRepository.findByAssessmentIdAndIsDeletedFalse(assessmentId).stream()
                .map(AssessmentDnaScoreJpaEntity::toDomain)
                .toList();
    }
}
