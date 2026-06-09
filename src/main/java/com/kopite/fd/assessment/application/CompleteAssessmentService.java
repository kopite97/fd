package com.kopite.fd.assessment.application;

import com.kopite.fd.assessment.domain.Assessment;
import com.kopite.fd.assessment.domain.AssessmentAnswer;
import com.kopite.fd.assessment.domain.AssessmentAnswerRepository;
import com.kopite.fd.assessment.domain.AssessmentDnaScore;
import com.kopite.fd.assessment.domain.AssessmentDnaScoreRepository;
import com.kopite.fd.assessment.domain.AssessmentRepository;
import com.kopite.fd.assessment.domain.AssessmentStatus;
import com.kopite.fd.assessment.domain.OptionScoreMapping;
import com.kopite.fd.assessment.domain.OptionScoreMappingRepository;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompleteAssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentAnswerRepository assessmentAnswerRepository;
    private final OptionScoreMappingRepository optionScoreMappingRepository;
    private final AssessmentDnaScoreRepository assessmentDnaScoreRepository;

    public CompleteAssessmentService(
            AssessmentRepository assessmentRepository,
            AssessmentAnswerRepository assessmentAnswerRepository,
            OptionScoreMappingRepository optionScoreMappingRepository,
            AssessmentDnaScoreRepository assessmentDnaScoreRepository
    ) {
        this.assessmentRepository = assessmentRepository;
        this.assessmentAnswerRepository = assessmentAnswerRepository;
        this.optionScoreMappingRepository = optionScoreMappingRepository;
        this.assessmentDnaScoreRepository = assessmentDnaScoreRepository;
    }

    @Transactional
    public CompleteAssessmentResult complete(CompleteAssessmentCommand command) {
        validate(command);

        Assessment assessment = assessmentRepository.findById(command.assessmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found."));

        if (assessment.getStatus() != AssessmentStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Only IN_PROGRESS assessments can be completed.");
        }

        List<AssessmentAnswer> assessmentAnswers = assessmentAnswerRepository.findByAssessmentId(command.assessmentId());
        if (assessmentAnswers.isEmpty()) {
            throw new IllegalArgumentException("Assessment cannot be completed without submitted answers.");
        }

        List<Long> optionIds = assessmentAnswers.stream()
                .map(AssessmentAnswer::getOptionId)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();

        List<OptionScoreMapping> optionScoreMappings = optionScoreMappingRepository.findByOptionIds(optionIds);
        Map<Long, List<OptionScoreMapping>> mappingsByOptionId = optionScoreMappings.stream()
                .collect(Collectors.groupingBy(OptionScoreMapping::getOptionId));

        Map<Long, Integer> scoreByDnaDefinitionId = assessmentAnswers.stream()
                .map(AssessmentAnswer::getOptionId)
                .filter(java.util.Objects::nonNull)
                .flatMap(optionId -> mappingsByOptionId.getOrDefault(optionId, List.of()).stream())
                .collect(Collectors.toMap(
                        OptionScoreMapping::getDnaDefinitionId,
                        OptionScoreMapping::getScoreDelta,
                        Integer::sum
                ));

        LocalDateTime now = LocalDateTime.now();

        List<AssessmentDnaScore> finalizedScores = scoreByDnaDefinitionId.entrySet().stream()
                .map(entry -> new AssessmentDnaScore(
                        null,
                        assessment.getId(),
                        entry.getKey(),
                        entry.getValue(),
                        now,
                        now
                ))
                .sorted(Comparator.comparing(AssessmentDnaScore::getDnaDefinitionId))
                .toList();

        List<AssessmentDnaScore> savedFinalizedScores = assessmentDnaScoreRepository.saveAll(finalizedScores);

        Assessment completedAssessment = new Assessment(
                assessment.getId(),
                assessment.getUserId(),
                assessment.getAnonymousId(),
                AssessmentStatus.COMPLETED,
                assessment.getQuestionVersion(),
                command.algorithmVersion().trim(),
                assessment.getClubDataVersion(),
                assessment.getStartedAt(),
                now,
                assessment.getCreatedAt(),
                now
        );

        Assessment savedCompletedAssessment = assessmentRepository.save(completedAssessment);

        List<CompleteAssessmentResult.FinalizedDnaScoreItem> finalizedDnaScoreItems = savedFinalizedScores.stream()
                .map(score -> new CompleteAssessmentResult.FinalizedDnaScoreItem(
                        score.getDnaDefinitionId(),
                        score.getScore()
                ))
                .toList();

        return new CompleteAssessmentResult(
                savedCompletedAssessment.getId(),
                savedCompletedAssessment.getStatus(),
                savedCompletedAssessment.getAlgorithmVersion(),
                savedCompletedAssessment.getCompletedAt(),
                finalizedDnaScoreItems
        );
    }

    private void validate(CompleteAssessmentCommand command) {
        if (command.assessmentId() == null) {
            throw new IllegalArgumentException("assessmentId must be present.");
        }

        if (command.algorithmVersion() == null || command.algorithmVersion().trim().isEmpty()) {
            throw new IllegalArgumentException("algorithmVersion must be present.");
        }
    }
}
