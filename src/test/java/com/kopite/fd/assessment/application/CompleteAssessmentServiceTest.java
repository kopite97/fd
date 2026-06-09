package com.kopite.fd.assessment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CompleteAssessmentServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private AssessmentAnswerRepository assessmentAnswerRepository;

    @Mock
    private OptionScoreMappingRepository optionScoreMappingRepository;

    @Mock
    private AssessmentDnaScoreRepository assessmentDnaScoreRepository;

    @InjectMocks
    private CompleteAssessmentService completeAssessmentService;

    @Test
    void shouldCompleteAssessmentAndAggregateMinimumDnaScores() {
        Assessment assessment = new Assessment(
                1L, null, "anon", AssessmentStatus.IN_PROGRESS, "v1", null, null,
                LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now()
        );
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));
        when(assessmentAnswerRepository.findByAssessmentId(1L)).thenReturn(List.of(
                new AssessmentAnswer(1L, 1L, 10L, 100L, null, null, LocalDateTime.now()),
                new AssessmentAnswer(2L, 1L, 11L, 100L, null, null, LocalDateTime.now()),
                new AssessmentAnswer(3L, 1L, 12L, 101L, null, null, LocalDateTime.now())
        ));
        when(optionScoreMappingRepository.findByOptionIds(List.of(100L, 101L))).thenReturn(List.of(
                new OptionScoreMapping(1L, 100L, 200L, 3),
                new OptionScoreMapping(2L, 101L, 200L, 2),
                new OptionScoreMapping(3L, 101L, 201L, 4)
        ));
        when(assessmentDnaScoreRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(assessmentRepository.save(any(Assessment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CompleteAssessmentResult result = completeAssessmentService.complete(
                new CompleteAssessmentCommand(1L, "alg-v1")
        );

        ArgumentCaptor<List<AssessmentDnaScore>> captor = ArgumentCaptor.forClass(List.class);
        verify(assessmentDnaScoreRepository).saveAll(captor.capture());
        List<AssessmentDnaScore> scores = captor.getValue();

        assertThat(scores).hasSize(2);
        assertThat(scores)
                .extracting(AssessmentDnaScore::getDnaDefinitionId, AssessmentDnaScore::getScore)
                .containsExactlyInAnyOrder(
                        org.assertj.core.groups.Tuple.tuple(200L, 8),
                        org.assertj.core.groups.Tuple.tuple(201L, 4)
                );
        assertThat(result.status()).isEqualTo(AssessmentStatus.COMPLETED);
        assertThat(result.algorithmVersion()).isEqualTo("alg-v1");
    }

    @Test
    void shouldRejectCompletionForCompletedAssessment() {
        Assessment assessment = new Assessment(
                1L, null, "anon", AssessmentStatus.COMPLETED, "v1", "alg-v1", null,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()
        );
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));

        assertThatThrownBy(() -> completeAssessmentService.complete(
                new CompleteAssessmentCommand(1L, "alg-v2")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Only IN_PROGRESS assessments can be completed.");
    }
}
