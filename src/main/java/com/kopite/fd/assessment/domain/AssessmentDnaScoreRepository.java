package com.kopite.fd.assessment.domain;

import java.util.List;

public interface AssessmentDnaScoreRepository {

    List<AssessmentDnaScore> saveAll(List<AssessmentDnaScore> assessmentDnaScores);

    List<AssessmentDnaScore> findByAssessmentId(Long assessmentId);
}
