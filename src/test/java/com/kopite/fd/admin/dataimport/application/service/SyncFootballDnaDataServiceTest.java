package com.kopite.fd.admin.dataimport.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.admin.dataimport.application.command.SyncFootballDnaDataCommand;
import com.kopite.fd.admin.dataimport.application.model.AcquiredImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import com.kopite.fd.admin.dataimport.application.model.ImportedRecord;
import com.kopite.fd.admin.dataimport.application.model.ImportedTargetData;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportPayload;
import com.kopite.fd.admin.dataimport.application.result.SyncFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.domain.model.DnaDefinitionSyncRef;
import com.kopite.fd.admin.dataimport.domain.model.FootballDnaDataSyncDataset;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaDataSyncRepository;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaImportModelConverter;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaImportTargetProvider;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaPayloadParser;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaSourceAcquirer;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SyncFootballDnaDataServiceTest {

    private static final ImportTarget CLUBS = target("clubs");
    private static final ImportTarget EMOTIONAL_DNA = target("emotional-dna");
    private static final ImportTarget PLAYSTYLE_DNA = target("playstyle-dna");
    private static final ImportTarget CLUB_STATUS = target("club-status");
    private static final ImportTarget CLUB_TAGS = target("club-tags");
    private static final ImportTarget DNA_RUBRIC = target("dna-rubric");

    @Mock
    private FootballDnaImportTargetProvider footballDnaImportTargetProvider;

    @Mock
    private FootballDnaSourceAcquirer footballDnaSourceAcquirer;

    @Mock
    private FootballDnaPayloadParser footballDnaPayloadParser;

    @Mock
    private FootballDnaImportModelConverter footballDnaImportModelConverter;

    @Mock
    private FootballDnaDataSyncRepository footballDnaDataSyncRepository;

    private SyncFootballDnaDataService syncFootballDnaDataService;

    @BeforeEach
    void setUp() {
        syncFootballDnaDataService = new SyncFootballDnaDataService(
                footballDnaImportTargetProvider,
                List.of(footballDnaSourceAcquirer),
                List.of(footballDnaPayloadParser),
                List.of(footballDnaImportModelConverter),
                footballDnaDataSyncRepository
        );
    }

    @Test
    void shouldNormalizeSourceDataAndSynchronizeDataset() {
        stubSuccessfulSourceLoading(validTargets());
        when(footballDnaDataSyncRepository.findActiveDnaDefinitions()).thenReturn(activeDnaDefinitions());
        when(footballDnaDataSyncRepository.existsClubDnaScoreDataVersion("club-v1")).thenReturn(false);
        when(footballDnaDataSyncRepository.synchronize(any())).thenReturn(List.of(
                new SyncFootballDnaDataResult.TableSyncResult("clubs", 1, 0, 0, 0, 0),
                new SyncFootballDnaDataResult.TableSyncResult("club_tags", 1, 0, 0, 0, 0),
                new SyncFootballDnaDataResult.TableSyncResult("club_dna_scores", 14, 0, 0, 0, 0)
        ));

        SyncFootballDnaDataResult result = syncFootballDnaDataService.sync(new SyncFootballDnaDataCommand("club-v1"));

        ArgumentCaptor<FootballDnaDataSyncDataset> captor =
                ArgumentCaptor.forClass(FootballDnaDataSyncDataset.class);
        verify(footballDnaDataSyncRepository).synchronize(captor.capture());
        FootballDnaDataSyncDataset dataset = captor.getValue();

        assertThat(result.success()).isTrue();
        assertThat(dataset.clubs()).hasSize(1);
        assertThat(dataset.clubs().get(0).code()).isEqualTo("ARS");
        assertThat(dataset.clubs().get(0).shortName()).isEqualTo("ARS");
        assertThat(dataset.clubs().get(0).competitionTier()).isEqualTo("TITLE_CONTENDER");
        assertThat(dataset.clubs().get(0).trendDirection()).isEqualTo("STABLE");
        assertThat(dataset.clubs().get(0).beginnerAccessibility()).isEqualByComparingTo("0.00");
        assertThat(dataset.clubTags()).hasSize(1);
        assertThat(dataset.clubTags().get(0).tagName()).isEqualTo("명문클럽");
        assertThat(dataset.clubTags().get(0).tagType()).isNull();
        assertThat(dataset.clubDnaScores()).hasSize(14);
        assertThat(dataset.clubDnaScores())
                .allSatisfy(score -> assertThat(score.dataVersion()).isEqualTo("club-v1"));
    }

    @Test
    void shouldRejectExistingDataVersionWithoutDatabaseWrites() {
        stubSuccessfulSourceLoading(validTargets());
        when(footballDnaDataSyncRepository.findActiveDnaDefinitions()).thenReturn(activeDnaDefinitions());
        when(footballDnaDataSyncRepository.existsClubDnaScoreDataVersion("club-v1")).thenReturn(true);

        SyncFootballDnaDataResult result = syncFootballDnaDataService.sync(new SyncFootballDnaDataCommand("club-v1"));

        assertThat(result.success()).isFalse();
        assertThat(result.validationErrors())
                .contains("club_dna_scores dataVersion already exists: club-v1");
        verify(footballDnaDataSyncRepository, never()).synchronize(any());
    }

    @Test
    void shouldRejectInvalidDnaScoreWithoutDatabaseWrites() {
        Map<String, ImportedTargetData> targets = validTargets();
        targets.put("emotional-dna", targetData(
                "emotional-dna",
                List.of("club_id", "club_name", "club_prestige", "fan_culture", "underdog", "growth",
                        "star_power", "drama", "local_identity", "popularity"),
                Map.of(
                        "club_id", "ARS",
                        "club_name", "Arsenal",
                        "club_prestige", "6",
                        "fan_culture", "4",
                        "underdog", "2",
                        "growth", "4",
                        "star_power", "4",
                        "drama", "4",
                        "local_identity", "3",
                        "popularity", "5"
                )
        ));
        stubSuccessfulSourceLoading(targets);
        when(footballDnaDataSyncRepository.findActiveDnaDefinitions()).thenReturn(activeDnaDefinitions());
        when(footballDnaDataSyncRepository.existsClubDnaScoreDataVersion("club-v1")).thenReturn(false);

        SyncFootballDnaDataResult result = syncFootballDnaDataService.sync(new SyncFootballDnaDataCommand("club-v1"));

        assertThat(result.success()).isFalse();
        assertThat(result.validationErrors())
                .anyMatch(error -> error.contains("Score out of range emotional-dna.club_prestige"));
        verify(footballDnaDataSyncRepository, never()).synchronize(any());
    }

    private void stubSuccessfulSourceLoading(Map<String, ImportedTargetData> targetsByKey) {
        List<ImportTarget> targets = List.of(CLUBS, EMOTIONAL_DNA, PLAYSTYLE_DNA, CLUB_STATUS, CLUB_TAGS, DNA_RUBRIC);
        when(footballDnaImportTargetProvider.getRequiredTargets()).thenReturn(targets);
        when(footballDnaSourceAcquirer.supports(ImportSourceType.CSV)).thenReturn(true);
        when(footballDnaPayloadParser.supports(ImportSourceType.CSV)).thenReturn(true);
        when(footballDnaImportModelConverter.supports(ImportSourceType.CSV)).thenReturn(true);

        for (ImportTarget target : targets) {
            when(footballDnaSourceAcquirer.acquire(target)).thenReturn(new AcquiredImportPayload(
                    target,
                    ImportSourceType.CSV,
                    target.targetKey() + "-raw"
            ));
        }

        when(footballDnaPayloadParser.parse(any())).thenAnswer(invocation -> {
            AcquiredImportPayload payload = invocation.getArgument(0);
            return new ParsedImportPayload(payload.target(), ImportSourceType.CSV, List.of(), List.of());
        });
        when(footballDnaImportModelConverter.convert(any())).thenAnswer(invocation -> {
            ParsedImportPayload payload = invocation.getArgument(0);
            return targetsByKey.get(payload.target().targetKey());
        });
    }

    private Map<String, ImportedTargetData> validTargets() {
        Map<String, ImportedTargetData> targets = new java.util.HashMap<>();
        targets.put("clubs", targetData(
                "clubs",
                List.of("club_id", "club_name", "league", "country", "is_active"),
                Map.of("club_id", "ARS", "club_name", "Arsenal", "league", "EPL", "country", "England",
                        "is_active", "TRUE")
        ));
        targets.put("club-status", targetData(
                "club-status",
                List.of("club_id", "club_name", "competition_tier", "trend"),
                Map.of("club_id", "ARS", "club_name", "Arsenal", "competition_tier", "우승후보", "trend", "안정기")
        ));
        targets.put("club-tags", targetData(
                "club-tags",
                List.of("club_id", "club_name", "tag", "display_order", "is_active"),
                Map.of("club_id", "ARS", "club_name", "Arsenal", "tag", "#명문클럽", "display_order", "1",
                        "is_active", "TRUE")
        ));
        targets.put("emotional-dna", targetData(
                "emotional-dna",
                List.of("club_id", "club_name", "club_prestige", "fan_culture", "underdog", "growth",
                        "star_power", "drama", "local_identity", "popularity"),
                Map.of(
                        "club_id", "ARS",
                        "club_name", "Arsenal",
                        "club_prestige", "5",
                        "fan_culture", "4",
                        "underdog", "2",
                        "growth", "4",
                        "star_power", "4",
                        "drama", "4",
                        "local_identity", "3",
                        "popularity", "5"
                )
        ));
        targets.put("playstyle-dna", targetData(
                "playstyle-dna",
                List.of("club_id", "club_name", "possession", "directness", "pressing", "organization",
                        "creativity", "transition_speed"),
                Map.of(
                        "club_id", "ARS",
                        "club_name", "Arsenal",
                        "possession", "5",
                        "directness", "3",
                        "pressing", "4",
                        "organization", "5",
                        "creativity", "3",
                        "transition_speed", "3"
                )
        ));
        targets.put("dna-rubric", targetData("dna-rubric", List.of("metric_key"), Map.of("metric_key", "fan_culture")));
        return targets;
    }

    private ImportedTargetData targetData(String targetKey, List<String> columns, Map<String, String> fields) {
        return new ImportedTargetData(
                targetKey,
                ImportSourceType.CSV,
                "https://example.com/" + targetKey + ".csv",
                columns,
                List.of(new ImportedRecord(fields))
        );
    }

    private List<DnaDefinitionSyncRef> activeDnaDefinitions() {
        return List.of(
                definition("EMOTIONAL", "club_prestige"),
                definition("EMOTIONAL", "fan_culture"),
                definition("EMOTIONAL", "underdog"),
                definition("EMOTIONAL", "growth"),
                definition("EMOTIONAL", "star_power"),
                definition("EMOTIONAL", "drama"),
                definition("EMOTIONAL", "local_identity"),
                definition("EMOTIONAL", "popularity"),
                definition("PLAYSTYLE", "possession"),
                definition("PLAYSTYLE", "directness"),
                definition("PLAYSTYLE", "pressing"),
                definition("PLAYSTYLE", "organization"),
                definition("PLAYSTYLE", "creativity"),
                definition("PLAYSTYLE", "transition_speed")
        );
    }

    private DnaDefinitionSyncRef definition(String category, String dnaKey) {
        return new DnaDefinitionSyncRef((long) (category + dnaKey).hashCode(), category, dnaKey);
    }

    private static ImportTarget target(String targetKey) {
        return new ImportTarget(targetKey, ImportSourceType.CSV, "https://example.com/" + targetKey + ".csv");
    }
}
