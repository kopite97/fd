package com.kopite.fd.assessment.domain;

import java.util.List;

public interface AssessmentAnswerRepository {

    AssessmentAnswer save(AssessmentAnswer assessmentAnswer);

    List<AssessmentAnswer> findByAssessmentId(Long assessmentId);
}
