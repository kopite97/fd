package com.kopite.fd.assessment.infrastructure.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.assessment.domain.model.Assessment;
import com.kopite.fd.assessment.domain.model.AssessmentAnswer;
import com.kopite.fd.assessment.domain.model.AssessmentDnaScore;
import com.kopite.fd.assessment.domain.model.AssessmentQuestion;
import com.kopite.fd.assessment.domain.model.AssessmentQuestionOption;
import com.kopite.fd.assessment.domain.model.DnaDefinition;
import com.kopite.fd.assessment.domain.model.OptionScoreMapping;
import com.kopite.fd.assessment.domain.type.AssessmentStatus;
import com.kopite.fd.assessment.infrastructure.entity.AssessmentAnswerJpaEntity;
import com.kopite.fd.assessment.infrastructure.entity.AssessmentDnaScoreJpaEntity;
import com.kopite.fd.assessment.infrastructure.entity.AssessmentJpaEntity;
import com.kopite.fd.assessment.infrastructure.entity.AssessmentQuestionJpaEntity;
import com.kopite.fd.assessment.infrastructure.entity.AssessmentQuestionOptionJpaEntity;
import com.kopite.fd.assessment.infrastructure.entity.DnaDefinitionJpaEntity;
import com.kopite.fd.assessment.infrastructure.entity.OptionScoreMappingJpaEntity;
import com.kopite.fd.assessment.infrastructure.repository.AssessmentAnswerJpaRepository;
import com.kopite.fd.assessment.infrastructure.repository.AssessmentDnaScoreJpaRepository;
import com.kopite.fd.assessment.infrastructure.repository.AssessmentJpaRepository;
import com.kopite.fd.assessment.infrastructure.repository.AssessmentQuestionJpaRepository;
import com.kopite.fd.assessment.infrastructure.repository.AssessmentQuestionOptionJpaRepository;
import com.kopite.fd.assessment.infrastructure.repository.DnaDefinitionJpaRepository;
import com.kopite.fd.assessment.infrastructure.repository.OptionScoreMappingJpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssessmentInfrastructureSoftDeleteTest {

    @Mock
    private AssessmentJpaRepository assessmentJpaRepository;

    @Mock
    private AssessmentAnswerJpaRepository assessmentAnswerJpaRepository;

    @Mock
    private AssessmentDnaScoreJpaRepository assessmentDnaScoreJpaRepository;

    @Mock
    private AssessmentQuestionJpaRepository assessmentQuestionJpaRepository;

    @Mock
    private AssessmentQuestionOptionJpaRepository assessmentQuestionOptionJpaRepository;

    @Mock
    private DnaDefinitionJpaRepository dnaDefinitionJpaRepository;

    @Mock
    private OptionScoreMappingJpaRepository optionScoreMappingJpaRepository;

    @InjectMocks
    private AssessmentPersistenceAdapter assessmentPersistenceAdapter;

    @InjectMocks
    private AssessmentAnswerPersistenceAdapter assessmentAnswerPersistenceAdapter;

    @InjectMocks
    private AssessmentDnaScorePersistenceAdapter assessmentDnaScorePersistenceAdapter;

    @InjectMocks
    private AssessmentQuestionPersistenceAdapter assessmentQuestionPersistenceAdapter;

    @InjectMocks
    private AssessmentQuestionOptionPersistenceAdapter assessmentQuestionOptionPersistenceAdapter;

    @InjectMocks
    private DnaDefinitionPersistenceAdapter dnaDefinitionPersistenceAdapter;

    @InjectMocks
    private OptionScoreMappingPersistenceAdapter optionScoreMappingPersistenceAdapter;

    @Test
    void shouldDefaultInheritedSoftDeleteToFalseForNewPersistenceEntities() {
        LocalDateTime now = LocalDateTime.now();

        Assessment assessment = new Assessment(
                1L,
                2L,
                null,
                AssessmentStatus.IN_PROGRESS,
                "q-v1",
                null,
                null,
                now,
                null,
                now,
                now
        );

        AssessmentAnswer answer = new AssessmentAnswer(
                3L,
                1L,
                10L,
                100L,
                null,
                null,
                now
        );

        AssessmentDnaScore dnaScore = new AssessmentDnaScore(
                4L,
                1L,
                200L,
                7,
                now,
                now
        );

        assertThat(AssessmentJpaEntity.fromDomain(assessment).isDeleted()).isFalse();
        assertThat(AssessmentAnswerJpaEntity.fromDomain(answer).isDeleted()).isFalse();
        assertThat(AssessmentDnaScoreJpaEntity.fromDomain(dnaScore).isDeleted()).isFalse();
    }

    @Test
    void shouldFindAssessmentByIdUsingNonDeletedScope() {
        AssessmentJpaEntity assessmentJpaEntity = org.mockito.Mockito.mock(AssessmentJpaEntity.class);
        Assessment assessment = new Assessment(
                1L,
                2L,
                null,
                AssessmentStatus.IN_PROGRESS,
                "q-v1",
                null,
                null,
                LocalDateTime.now(),
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(assessmentJpaRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(assessmentJpaEntity));
        when(assessmentJpaEntity.toDomain()).thenReturn(assessment);

        Optional<Assessment> result = assessmentPersistenceAdapter.findById(1L);

        assertThat(result).contains(assessment);
        verify(assessmentJpaRepository).findByIdAndIsDeletedFalse(1L);
    }

    @Test
    void shouldFindAssessmentAnswersUsingNonDeletedScope() {
        AssessmentAnswerJpaEntity answerJpaEntity = org.mockito.Mockito.mock(AssessmentAnswerJpaEntity.class);
        AssessmentAnswer answer = new AssessmentAnswer(1L, 1L, 10L, 100L, null, null, LocalDateTime.now());
        when(assessmentAnswerJpaRepository.findByAssessmentIdAndIsDeletedFalse(1L)).thenReturn(List.of(answerJpaEntity));
        when(answerJpaEntity.toDomain()).thenReturn(answer);

        List<AssessmentAnswer> result = assessmentAnswerPersistenceAdapter.findByAssessmentId(1L);

        assertThat(result).containsExactly(answer);
        verify(assessmentAnswerJpaRepository).findByAssessmentIdAndIsDeletedFalse(1L);
    }

    @Test
    void shouldFindAssessmentDnaScoresUsingNonDeletedScope() {
        AssessmentDnaScoreJpaEntity dnaScoreJpaEntity = org.mockito.Mockito.mock(AssessmentDnaScoreJpaEntity.class);
        AssessmentDnaScore dnaScore = new AssessmentDnaScore(1L, 1L, 200L, 9, LocalDateTime.now(), LocalDateTime.now());
        when(assessmentDnaScoreJpaRepository.findByAssessmentIdAndIsDeletedFalse(1L))
                .thenReturn(List.of(dnaScoreJpaEntity));
        when(dnaScoreJpaEntity.toDomain()).thenReturn(dnaScore);

        List<AssessmentDnaScore> result = assessmentDnaScorePersistenceAdapter.findByAssessmentId(1L);

        assertThat(result).containsExactly(dnaScore);
        verify(assessmentDnaScoreJpaRepository).findByAssessmentIdAndIsDeletedFalse(1L);
    }

    @Test
    void shouldFindQuestionsUsingNonDeletedScope() {
        AssessmentQuestionJpaEntity questionJpaEntity = org.mockito.Mockito.mock(AssessmentQuestionJpaEntity.class);
        AssessmentQuestion question = new AssessmentQuestion(10L, "Q1", "CHOICE", 100L, 1, "q-v1");
        when(assessmentQuestionJpaRepository.findByQuestionVersionAndActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc("q-v1"))
                .thenReturn(List.of(questionJpaEntity));
        when(questionJpaEntity.toDomain()).thenReturn(question);

        List<AssessmentQuestion> result = assessmentQuestionPersistenceAdapter.findActiveByQuestionVersion("q-v1");

        assertThat(result).containsExactly(question);
        verify(assessmentQuestionJpaRepository)
                .findByQuestionVersionAndActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc("q-v1");
    }

    @Test
    void shouldRoundTripStringBasedVersionIdentifiersForAssessmentPersistenceEntity() {
        LocalDateTime now = LocalDateTime.now();
        Assessment assessment = new Assessment(
                1L,
                2L,
                "anon-123",
                AssessmentStatus.COMPLETED,
                "q-v1",
                "alg-v1",
                "club-v1",
                now,
                now,
                now,
                now
        );

        Assessment roundTrip = AssessmentJpaEntity.fromDomain(assessment).toDomain();

        assertThat(roundTrip.getQuestionVersion()).isEqualTo("q-v1");
        assertThat(roundTrip.getAlgorithmVersion()).isEqualTo("alg-v1");
        assertThat(roundTrip.getClubDataVersion()).isEqualTo("club-v1");
    }

    @Test
    void shouldFindQuestionOptionsUsingNonDeletedScope() {
        AssessmentQuestionOptionJpaEntity optionJpaEntity = org.mockito.Mockito.mock(AssessmentQuestionOptionJpaEntity.class);
        AssessmentQuestionOption option = new AssessmentQuestionOption(100L, 10L, "A", 1);
        List<Long> questionIds = List.of(10L);
        when(assessmentQuestionOptionJpaRepository
                .findByQuestionIdInAndActiveTrueAndIsDeletedFalseOrderByQuestionIdAscDisplayOrderAsc(questionIds))
                .thenReturn(List.of(optionJpaEntity));
        when(optionJpaEntity.toDomain()).thenReturn(option);

        List<AssessmentQuestionOption> result = assessmentQuestionOptionPersistenceAdapter.findActiveByQuestionIds(questionIds);

        assertThat(result).containsExactly(option);
        verify(assessmentQuestionOptionJpaRepository)
                .findByQuestionIdInAndActiveTrueAndIsDeletedFalseOrderByQuestionIdAscDisplayOrderAsc(questionIds);
    }

    @Test
    void shouldFindActiveDnaDefinitionsUsingNonDeletedScope() {
        DnaDefinitionJpaEntity dnaDefinitionJpaEntity = org.mockito.Mockito.mock(DnaDefinitionJpaEntity.class);
        DnaDefinition dnaDefinition = new DnaDefinition(1L, "EMOTIONAL", "club_prestige", "Prestige", null, 1);
        when(dnaDefinitionJpaRepository.findByActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc())
                .thenReturn(List.of(dnaDefinitionJpaEntity));
        when(dnaDefinitionJpaEntity.toDomain()).thenReturn(dnaDefinition);

        List<DnaDefinition> result = dnaDefinitionPersistenceAdapter.findActiveDefinitions();

        assertThat(result).containsExactly(dnaDefinition);
        verify(dnaDefinitionJpaRepository).findByActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc();
    }

    @Test
    void shouldFindOptionScoreMappingsUsingNonDeletedScope() {
        OptionScoreMappingJpaEntity optionScoreMappingJpaEntity = org.mockito.Mockito.mock(OptionScoreMappingJpaEntity.class);
        OptionScoreMapping optionScoreMapping = new OptionScoreMapping(1L, 100L, 200L, 5);
        List<Long> optionIds = List.of(100L);
        when(optionScoreMappingJpaRepository.findByOptionIdInAndIsDeletedFalse(optionIds))
                .thenReturn(List.of(optionScoreMappingJpaEntity));
        when(optionScoreMappingJpaEntity.toDomain()).thenReturn(optionScoreMapping);

        List<OptionScoreMapping> result = optionScoreMappingPersistenceAdapter.findByOptionIds(optionIds);

        assertThat(result).containsExactly(optionScoreMapping);
        verify(optionScoreMappingJpaRepository).findByOptionIdInAndIsDeletedFalse(optionIds);
    }
}
