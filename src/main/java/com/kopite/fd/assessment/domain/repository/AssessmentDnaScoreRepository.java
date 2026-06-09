package com.kopite.fd.assessment.domain.repository;

import com.kopite.fd.assessment.domain.model.AssessmentDnaScore;
import java.util.List;

public interface AssessmentDnaScoreRepository {

    List<AssessmentDnaScore> saveAll(List<AssessmentDnaScore> assessmentDnaScores);

    List<AssessmentDnaScore> findByAssessmentId(Long assessmentId);
}
