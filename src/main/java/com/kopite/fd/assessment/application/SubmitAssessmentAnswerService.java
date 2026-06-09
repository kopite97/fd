package com.kopite.fd.assessment.application;

import com.kopite.fd.assessment.domain.Assessment;
import com.kopite.fd.assessment.domain.AssessmentAnswer;
import com.kopite.fd.assessment.domain.AssessmentAnswerRepository;
import com.kopite.fd.assessment.domain.AssessmentRepository;
import com.kopite.fd.assessment.domain.AssessmentStatus;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SubmitAssessmentAnswerService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentAnswerRepository assessmentAnswerRepository;

    public SubmitAssessmentAnswerService(
            AssessmentRepository assessmentRepository,
            AssessmentAnswerRepository assessmentAnswerRepository
    ) {
        this.assessmentRepository = assessmentRepository;
        this.assessmentAnswerRepository = assessmentAnswerRepository;
    }

    @Transactional
    public SubmitAssessmentAnswerResult submit(SubmitAssessmentAnswerCommand command) {
        Assessment assessment = assessmentRepository.findById(command.assessmentId())
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found."));

        if (assessment.getStatus() != AssessmentStatus.IN_PROGRESS) {
            throw new IllegalArgumentException("Answers can only be submitted for an active assessment.");
        }

        LocalDateTime now = LocalDateTime.now();
        AssessmentAnswer assessmentAnswer = new AssessmentAnswer(
                null,
                command.assessmentId(),
                command.questionId(),
                command.optionId(),
                command.answerText(),
                null,
                now
        );

        AssessmentAnswer savedAssessmentAnswer = assessmentAnswerRepository.save(assessmentAnswer);

        return new SubmitAssessmentAnswerResult(
                savedAssessmentAnswer.getId(),
                savedAssessmentAnswer.getAssessmentId(),
                savedAssessmentAnswer.getQuestionId(),
                savedAssessmentAnswer.getOptionId(),
                savedAssessmentAnswer.getAnswerText(),
                savedAssessmentAnswer.getCreatedAt()
        );
    }
}
