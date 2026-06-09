package com.kopite.fd.assessment.dto.response;

import com.kopite.fd.assessment.application.CompleteAssessmentResult;
import com.kopite.fd.assessment.domain.AssessmentStatus;
import java.time.LocalDateTime;
import java.util.List;

public record CompleteAssessmentResponse(
        Long assessmentId,
        AssessmentStatus status,
        String algorithmVersion,
        LocalDateTime completedAt,
        List<FinalizedDnaScoreItem> finalizedDnaScores
) {

    public static CompleteAssessmentResponse from(CompleteAssessmentResult result) {
        return new CompleteAssessmentResponse(
                result.assessmentId(),
                result.status(),
                result.algorithmVersion(),
                result.completedAt(),
                result.finalizedDnaScores().stream()
                        .map(score -> new FinalizedDnaScoreItem(
                                score.dnaDefinitionId(),
                                score.score()
                        ))
                        .toList()
        );
    }

    public record FinalizedDnaScoreItem(
            Long dnaDefinitionId,
            int score
    ) {
    }
}
