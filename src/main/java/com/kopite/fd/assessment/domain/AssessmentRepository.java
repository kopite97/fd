package com.kopite.fd.assessment.domain;

import java.util.Optional;

public interface AssessmentRepository {

    Assessment save(Assessment assessment);

    Optional<Assessment> findById(Long assessmentId);
}
