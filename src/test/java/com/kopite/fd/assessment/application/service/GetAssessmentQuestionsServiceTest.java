package com.kopite.fd.assessment.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.assessment.application.query.GetAssessmentQuestionsQuery;
import com.kopite.fd.assessment.application.result.GetAssessmentQuestionsResult;
import com.kopite.fd.assessment.domain.model.Assessment;
import com.kopite.fd.assessment.domain.model.AssessmentQuestion;
import com.kopite.fd.assessment.domain.model.AssessmentQuestionOption;
import com.kopite.fd.assessment.domain.repository.AssessmentQuestionOptionRepository;
import com.kopite.fd.assessment.domain.repository.AssessmentQuestionRepository;
import com.kopite.fd.assessment.domain.repository.AssessmentRepository;
import com.kopite.fd.assessment.domain.type.AssessmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetAssessmentQuestionsServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private AssessmentQuestionRepository assessmentQuestionRepository;

    @Mock
    private AssessmentQuestionOptionRepository assessmentQuestionOptionRepository;

    @InjectMocks
    private GetAssessmentQuestionsService getAssessmentQuestionsService;

    @Test
    void shouldReturnQuestionsUsingFrozenQuestionVersionForInProgressAssessment() {
        Assessment assessment = new Assessment(
                1L, null, "anon", AssessmentStatus.IN_PROGRESS, "v2", null, null,
                LocalDateTime.now(), null, LocalDateTime.now(), LocalDateTime.now()
        );
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));
        when(assessmentQuestionRepository.findActiveByQuestionVersion("v2")).thenReturn(List.of(
                new AssessmentQuestion(10L, "Q1", "CHOICE", 100L, 1, "v2")
        ));
        when(assessmentQuestionOptionRepository.findActiveByQuestionIds(List.of(10L))).thenReturn(List.of(
                new AssessmentQuestionOption(1000L, 10L, "A", 1),
                new AssessmentQuestionOption(1001L, 10L, "B", 2)
        ));

        GetAssessmentQuestionsResult result = getAssessmentQuestionsService.getQuestions(
                new GetAssessmentQuestionsQuery(1L)
        );

        verify(assessmentQuestionRepository).findActiveByQuestionVersion("v2");
        assertThat(result.questionVersion()).isEqualTo("v2");
        assertThat(result.questions()).hasSize(1);
        assertThat(result.questions().get(0).options()).hasSize(2);
    }

    @Test
    void shouldRejectWhenAssessmentIsCompleted() {
        Assessment assessment = new Assessment(
                1L, null, "anon", AssessmentStatus.COMPLETED, "v2", "alg-1", null,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()
        );
        when(assessmentRepository.findById(1L)).thenReturn(Optional.of(assessment));

        assertThatThrownBy(() -> getAssessmentQuestionsService.getQuestions(new GetAssessmentQuestionsQuery(1L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Questions can only be retrieved for an active assessment.");
    }
}
