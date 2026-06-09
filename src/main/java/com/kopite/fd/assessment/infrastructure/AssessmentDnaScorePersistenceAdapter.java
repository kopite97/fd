package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.AssessmentDnaScore;
import com.kopite.fd.assessment.domain.AssessmentDnaScoreRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AssessmentDnaScorePersistenceAdapter implements AssessmentDnaScoreRepository {

    private final AssessmentDnaScoreJpaRepository assessmentDnaScoreJpaRepository;

    public AssessmentDnaScorePersistenceAdapter(AssessmentDnaScoreJpaRepository assessmentDnaScoreJpaRepository) {
        this.assessmentDnaScoreJpaRepository = assessmentDnaScoreJpaRepository;
    }

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
        return assessmentDnaScoreJpaRepository.findByAssessmentId(assessmentId).stream()
                .map(AssessmentDnaScoreJpaEntity::toDomain)
                .toList();
    }
}
