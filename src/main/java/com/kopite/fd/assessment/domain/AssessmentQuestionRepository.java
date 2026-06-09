package com.kopite.fd.assessment.domain;

import java.util.List;

public interface AssessmentQuestionRepository {

    List<AssessmentQuestion> findActiveByQuestionVersion(String questionVersion);
}
