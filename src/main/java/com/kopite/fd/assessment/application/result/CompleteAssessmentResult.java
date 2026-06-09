package com.kopite.fd.assessment.application.result;

import com.kopite.fd.assessment.domain.type.AssessmentStatus;
import java.time.LocalDateTime;
import java.util.List;

public record CompleteAssessmentResult(
        Long assessmentId,
        AssessmentStatus status,
        String algorithmVersion,
        LocalDateTime completedAt,
        List<FinalizedDnaScoreItem> finalizedDnaScores
) {

    public record FinalizedDnaScoreItem(
            Long dnaDefinitionId,
            int score
    ) {
    }
}
