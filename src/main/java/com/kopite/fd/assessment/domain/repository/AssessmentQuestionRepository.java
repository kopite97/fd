package com.kopite.fd.assessment.domain.repository;

import com.kopite.fd.assessment.domain.model.AssessmentQuestion;
import java.util.List;

public interface AssessmentQuestionRepository {

    List<AssessmentQuestion> findActiveByQuestionVersion(String questionVersion);
}
