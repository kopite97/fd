package com.kopite.fd.assessment.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.assessment.application.command.StartAssessmentCommand;
import com.kopite.fd.assessment.application.result.StartAssessmentResult;
import com.kopite.fd.assessment.domain.model.Assessment;
import com.kopite.fd.assessment.domain.repository.AssessmentRepository;
import com.kopite.fd.assessment.domain.type.AssessmentStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StartAssessmentServiceTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @InjectMocks
    private StartAssessmentService startAssessmentService;

    @Test
    void shouldCreateAssessmentWhenIdentityIsValid() {
        when(assessmentRepository.save(any(Assessment.class))).thenAnswer(invocation -> {
            Assessment assessment = invocation.getArgument(0);
            return new Assessment(
                    1L,
                    assessment.getUserId(),
                    assessment.getAnonymousId(),
                    assessment.getStatus(),
                    assessment.getQuestionVersion(),
                    assessment.getAlgorithmVersion(),
                    assessment.getClubDataVersion(),
                    assessment.getStartedAt(),
                    assessment.getCompletedAt(),
                    assessment.getCreatedAt(),
                    assessment.getUpdatedAt()
            );
        });

        StartAssessmentResult result = startAssessmentService.start(
                new StartAssessmentCommand(null, " anon-123 ", "q-v1")
        );

        ArgumentCaptor<Assessment> captor = ArgumentCaptor.forClass(Assessment.class);
        verify(assessmentRepository).save(captor.capture());
        Assessment savedAssessment = captor.getValue();

        assertThat(savedAssessment.getStatus()).isEqualTo(AssessmentStatus.IN_PROGRESS);
        assertThat(savedAssessment.getQuestionVersion()).isEqualTo("q-v1");
        assertThat(savedAssessment.getAnonymousId()).isEqualTo("anon-123");
        assertThat(savedAssessment.getAlgorithmVersion()).isNull();
        assertThat(savedAssessment.getClubDataVersion()).isNull();
        assertThat(savedAssessment.getCompletedAt()).isNull();

        assertThat(result.assessmentId()).isEqualTo(1L);
        assertThat(result.status()).isEqualTo(AssessmentStatus.IN_PROGRESS);
        assertThat(result.questionVersion()).isEqualTo("q-v1");
        assertThat(result.anonymousId()).isEqualTo("anon-123");
    }

    @Test
    void shouldRejectWhenUserIdAndAnonymousIdAreMissing() {
        assertThatThrownBy(() -> startAssessmentService.start(
                new StartAssessmentCommand(null, "  ", "q-v1")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least one of userId or anonymousId must be present.");
    }
}
