package com.kopite.fd.assessment.dto.request;

import jakarta.validation.constraints.NotBlank;

public record StartAssessmentRequest(
        Long userId,
        String anonymousId,
        @NotBlank String questionVersion
) {
}
