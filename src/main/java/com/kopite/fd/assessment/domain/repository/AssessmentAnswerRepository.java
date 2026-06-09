package com.kopite.fd.assessment.domain.repository;

import com.kopite.fd.assessment.domain.model.AssessmentAnswer;
import java.util.List;

public interface AssessmentAnswerRepository {

    AssessmentAnswer save(AssessmentAnswer assessmentAnswer);

    List<AssessmentAnswer> findByAssessmentId(Long assessmentId);
}
