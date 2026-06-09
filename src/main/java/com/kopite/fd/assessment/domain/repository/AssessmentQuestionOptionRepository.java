package com.kopite.fd.assessment.domain.repository;

import com.kopite.fd.assessment.domain.model.AssessmentQuestionOption;
import java.util.List;

public interface AssessmentQuestionOptionRepository {

    List<AssessmentQuestionOption> findActiveByQuestionIds(List<Long> questionIds);
}
