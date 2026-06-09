package com.kopite.fd.assessment.domain.repository;

import com.kopite.fd.assessment.domain.model.Assessment;
import java.util.Optional;

public interface AssessmentRepository {

    Assessment save(Assessment assessment);

    Optional<Assessment> findById(Long assessmentId);
}
