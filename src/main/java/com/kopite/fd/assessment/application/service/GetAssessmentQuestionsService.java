package com.kopite.fd.assessment.application.service;

import com.kopite.fd.assessment.application.query.GetAssessmentQuestionsQuery;
import com.kopite.fd.assessment.application.result.GetAssessmentQuestionsResult;
import com.kopite.fd.assessment.domain.model.Assessment;
import com.kopite.fd.assessment.domain.model.AssessmentQuestion;
import com.kopite.fd.assessment.domain.model.AssessmentQuestionOption;
import com.kopite.fd.assessment.domain.repository.AssessmentQuestionOptionRepository;
import com.kopite.fd.assessment.domain.repository.AssessmentQuestionRepository;
import com.kopite.fd.assessment.domain.repository.AssessmentRepository;
import com.kopite.fd.assessment.domain.type.AssessmentStatus;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetAssessmentQuestionsService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentQuestionRepository assessmentQuestionRepository;
    private final AssessmentQuestionOptionRepository assessmentQuestionOptionRepository;

    public GetAssessmentQuestionsService(
            AssessmentRepository assessmentRepository,
            AssessmentQuestionRepository assessmentQuestionRepository,
            AssessmentQuestionOptionRepository assessmentQuestionOptionRepository
    ) {
        this.assessmentRepository = assessmentRepository;
        this.assessmentQuestionRepository = assessmentQuestionRepository;
        this.assessmentQuestionOptionRepository = assessmentQuestionOptionRepository;
    }

    @Transactional(readOnly = true)
    public GetAssessmentQuestionsResult getQuestions(GetAssessmentQuestionsQuery query) {
        Assessment assessment = assessmentRepository.findById(query.assessmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found."));

        if (assessment.getStatus() != AssessmentStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Questions can only be retrieved for an active assessment.");
        }

        String questionVersion = assessment.getQuestionVersion();
        List<AssessmentQuestion> questions = assessmentQuestionRepository.findActiveByQuestionVersion(questionVersion);
        List<Long> questionIds = questions.stream()
                .map(AssessmentQuestion::getId)
                .toList();
        List<AssessmentQuestionOption> options = assessmentQuestionOptionRepository.findActiveByQuestionIds(questionIds);

        Map<Long, List<GetAssessmentQuestionsResult.AssessmentQuestionOptionItem>> optionsByQuestionId = options.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        AssessmentQuestionOption::getQuestionId,
                        java.util.LinkedHashMap::new,
                        java.util.stream.Collectors.mapping(
                                option -> new GetAssessmentQuestionsResult.AssessmentQuestionOptionItem(
                                        option.getId(),
                                        option.getOptionText(),
                                        option.getDisplayOrder()
                                ),
                                java.util.stream.Collectors.toList()
                        )
                ));

        List<GetAssessmentQuestionsResult.AssessmentQuestionItem> questionItems = questions.stream()
                .map(question -> new GetAssessmentQuestionsResult.AssessmentQuestionItem(
                        question.getId(),
                        question.getQuestionText(),
                        question.getQuestionType(),
                        question.getPrimaryDnaDefinitionId(),
                        question.getDisplayOrder(),
                        optionsByQuestionId.getOrDefault(question.getId(), List.of())
                ))
                .toList();

        return new GetAssessmentQuestionsResult(
                assessment.getId(),
                questionVersion,
                questionItems
        );
    }
}
