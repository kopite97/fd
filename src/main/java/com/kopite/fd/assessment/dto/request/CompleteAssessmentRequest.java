package com.kopite.fd.assessment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CompleteAssessmentRequest(
        @NotBlank String algorithmVersion
) {
}
