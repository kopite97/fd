package com.kopite.fd.assessment.domain;

import java.util.List;

public interface AssessmentQuestionOptionRepository {

    List<AssessmentQuestionOption> findActiveByQuestionIds(List<Long> questionIds);
}
