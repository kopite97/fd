package com.kopite.fd.assessment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.assessment.domain.Assessment;
import com.kopite.fd.assessment.domain.AssessmentAnswer;
import com.kopite.fd.assessment.domain.AssessmentAnswerRepository;
import com.kopite.fd.assessment.domain.AssessmentRepository;
import com.kopite.fd.assessment.domain.AssessmentStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubmitAssessmentAnswerServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private AssessmentAnswerRepository assessmentAnswerRepository;

    @InjectMocks
    private SubmitAssessmentAnswerService submitAssessmentAnswerService;

    @Test
    void shouldPersistAnswerForInProgressAssessment() {
        Assessment assessment = new Assessment(
                1L, null, "anon", AssessmentStatus.IN_PROGRESS, "v1", null, null,
                LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now()
        );
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));
        when(assessmentAnswerRepository.save(any(AssessmentAnswer.class))).thenAnswer(invocation -> {
            AssessmentAnswer answer = invocation.getArgument(0);
            return new AssessmentAnswer(
                    99L,
                    answer.getAssessmentId(),
                    answer.getQuestionId(),
                    answer.getOptionId(),
                    answer.getAnswerText(),
                    answer.getScoreSnapshotJson(),
                    answer.getCreatedAt()
            );
        });

        SubmitAssessmentAnswerResult result = submitAssessmentAnswerService.submit(
                new SubmitAssessmentAnswerCommand(1L, 10L, 1000L, null)
        );

        ArgumentCaptor<AssessmentAnswer> captor = ArgumentCaptor.forClass(AssessmentAnswer.class);
        verify(assessmentAnswerRepository).save(captor.capture());
        assertThat(captor.getValue().getAssessmentId()).isEqualTo(1L);
        assertThat(captor.getValue().getQuestionId()).isEqualTo(10L);
        assertThat(result.answerId()).isEqualTo(99L);
    }

    @Test
    void shouldRejectAnswerSubmissionForCompletedAssessment() {
        Assessment assessment = new Assessment(
                1L, null, "anon", AssessmentStatus.COMPLETED, "v1", "alg-1", null,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()
        );
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));

        assertThatThrownBy(() -> submitAssessmentAnswerService.submit(
                new SubmitAssessmentAnswerCommand(1L, 10L, 1000L, null)
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Answers can only be submitted for an active assessment.");
    }
}
