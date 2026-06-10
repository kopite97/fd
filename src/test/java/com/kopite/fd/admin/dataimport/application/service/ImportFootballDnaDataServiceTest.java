package com.kopite.fd.admin.dataimport.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.admin.dataimport.application.model.AcquiredImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import com.kopite.fd.admin.dataimport.application.model.ImportedRecord;
import com.kopite.fd.admin.dataimport.application.model.ImportedTargetData;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportRecord;
import com.kopite.fd.admin.dataimport.application.result.ImportFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaDataArtifactRepository;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaImportModelConverter;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaImportTargetProvider;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaPayloadParser;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaSourceAcquirer;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ImportFootballDnaDataServiceTest {

    @Mock
    private FootballDnaImportTargetProvider footballDnaImportTargetProvider;

    @Mock
    private FootballDnaSourceAcquirer footballDnaSourceAcquirer;

    @Mock
    private FootballDnaPayloadParser footballDnaPayloadParser;

    @Mock
    private FootballDnaImportModelConverter footballDnaImportModelConverter;

    @Mock
    private FootballDnaDataArtifactRepository footballDnaDataArtifactRepository;

    @Test
    void shouldImportConfiguredTargetsThroughAbstractionsAndPersistAggregatedArtifact() {
        ImportTarget clubsTarget = new ImportTarget("clubs", ImportSourceType.CSV, "https://example.com/clubs.csv");
        ImportTarget tagsTarget = new ImportTarget("club-tags", ImportSourceType.CSV, "https://example.com/tags.csv");

        when(footballDnaImportTargetProvider.getRequiredTargets()).thenReturn(List.of(clubsTarget, tagsTarget));
        when(footballDnaSourceAcquirer.supports(ImportSourceType.CSV)).thenReturn(true);
        when(footballDnaPayloadParser.supports(ImportSourceType.CSV)).thenReturn(true);
        when(footballDnaImportModelConverter.supports(ImportSourceType.CSV)).thenReturn(true);

        when(footballDnaSourceAcquirer.acquire(clubsTarget)).thenReturn(new AcquiredImportPayload(
                clubsTarget,
                ImportSourceType.CSV,
                "club,name\nLIV,Liverpool"
        ));
        when(footballDnaSourceAcquirer.acquire(tagsTarget)).thenReturn(new AcquiredImportPayload(
                tagsTarget,
                ImportSourceType.CSV,
                "club,tag\nLIV,YNWA"
        ));

        when(footballDnaPayloadParser.parse(any())).thenReturn(
                new ParsedImportPayload(
                        clubsTarget,
                        ImportSourceType.CSV,
                        List.of("club", "name"),
                        List.of(new ParsedImportRecord(2, Map.of("club", "LIV", "name", "Liverpool")))
                ),
                new ParsedImportPayload(
                        tagsTarget,
                        ImportSourceType.CSV,
                        List.of("club", "tag"),
                        List.of(new ParsedImportRecord(2, Map.of("club", "LIV", "tag", "YNWA")))
                )
        );

        when(footballDnaImportModelConverter.convert(any())).thenReturn(
                new ImportedTargetData(
                        "clubs",
                        ImportSourceType.CSV,
                        "https://example.com/clubs.csv",
                        List.of("club", "name"),
                        List.of(new ImportedRecord(Map.of("club", "LIV", "name", "Liverpool")))
                ),
                new ImportedTargetData(
                        "club-tags",
                        ImportSourceType.CSV,
                        "https://example.com/tags.csv",
                        List.of("club", "tag"),
                        List.of(new ImportedRecord(Map.of("club", "LIV", "tag", "YNWA")))
                )
        );

        when(footballDnaDataArtifactRepository.save(eq(true), any(), any(), any()))
                .thenReturn(new FootballDnaDataArtifactRepository.SavedArtifact(
                        "src/main/resources/data/import.md",
                        256L,
                        2
                ));

        ImportFootballDnaDataResult result = new ImportFootballDnaDataService(
                footballDnaImportTargetProvider,
                List.of(footballDnaSourceAcquirer),
                List.of(footballDnaPayloadParser),
                List.of(footballDnaImportModelConverter),
                footballDnaDataArtifactRepository
        ).importData();

        verify(footballDnaSourceAcquirer).acquire(clubsTarget);
        verify(footballDnaSourceAcquirer).acquire(tagsTarget);
        assertThat(result.success()).isTrue();
        assertThat(result.artifactPath()).isEqualTo("src/main/resources/data/import.md");
        assertThat(result.processedTargetCount()).isEqualTo(2);
        assertThat(result.targetResults()).hasSize(2);
        assertThat(result.targetResults())
                .extracting(ImportFootballDnaDataResult.TargetImportResult::targetKey)
                .containsExactly("clubs", "club-tags");
    }

    @Test
    void shouldFailFastAndReturnPerTargetResultsWhenTargetImportFails() {
        ImportTarget clubsTarget = new ImportTarget("clubs", ImportSourceType.CSV, "https://example.com/clubs.csv");
        ImportTarget tagsTarget = new ImportTarget("club-tags", ImportSourceType.CSV, "https://example.com/tags.csv");

        when(footballDnaImportTargetProvider.getRequiredTargets()).thenReturn(List.of(clubsTarget, tagsTarget));
        when(footballDnaSourceAcquirer.supports(ImportSourceType.CSV)).thenReturn(true);
        when(footballDnaPayloadParser.supports(ImportSourceType.CSV)).thenReturn(true);
        when(footballDnaImportModelConverter.supports(ImportSourceType.CSV)).thenReturn(true);

        when(footballDnaSourceAcquirer.acquire(clubsTarget)).thenReturn(new AcquiredImportPayload(
                clubsTarget,
                ImportSourceType.CSV,
                "club,name\nLIV,Liverpool"
        ));
        when(footballDnaSourceAcquirer.acquire(tagsTarget))
                .thenThrow(new IllegalStateException("Failed to fetch published CSV for target 'club-tags'."));

        when(footballDnaPayloadParser.parse(any())).thenReturn(new ParsedImportPayload(
                clubsTarget,
                ImportSourceType.CSV,
                List.of("club", "name"),
                List.of(new ParsedImportRecord(2, Map.of("club", "LIV", "name", "Liverpool")))
        ));
        when(footballDnaImportModelConverter.convert(any())).thenReturn(new ImportedTargetData(
                "clubs",
                ImportSourceType.CSV,
                "https://example.com/clubs.csv",
                List.of("club", "name"),
                List.of(new ImportedRecord(Map.of("club", "LIV", "name", "Liverpool")))
        ));

        when(footballDnaDataArtifactRepository.save(eq(false), any(), any(), any()))
                .thenReturn(new FootballDnaDataArtifactRepository.SavedArtifact(
                        "src/main/resources/data/import-failure.md",
                        200L,
                        2
                ));

        ImportFootballDnaDataResult result = new ImportFootballDnaDataService(
                footballDnaImportTargetProvider,
                List.of(footballDnaSourceAcquirer),
                List.of(footballDnaPayloadParser),
                List.of(footballDnaImportModelConverter),
                footballDnaDataArtifactRepository
        ).importData();

        assertThat(result.success()).isFalse();
        assertThat(result.targetResults()).hasSize(2);
        assertThat(result.targetResults().get(0).status())
                .isEqualTo(ImportFootballDnaDataResult.TargetImportStatus.SUCCEEDED);
        assertThat(result.targetResults().get(1).status())
                .isEqualTo(ImportFootballDnaDataResult.TargetImportStatus.FAILED);
        assertThat(result.targetResults().get(1).failureMessage())
                .isEqualTo("Failed to fetch published CSV for target 'club-tags'.");
    }

    @Test
    void shouldReturnFailedResultWhenNoSourceAcquirerSupportsConfiguredSourceType() {
        ImportTarget clubsTarget = new ImportTarget("clubs", ImportSourceType.CSV, "https://example.com/clubs.csv");
        when(footballDnaImportTargetProvider.getRequiredTargets()).thenReturn(List.of(clubsTarget));
        when(footballDnaDataArtifactRepository.save(eq(false), any(), any(), any()))
                .thenReturn(new FootballDnaDataArtifactRepository.SavedArtifact(
                        "src/main/resources/data/import-failure.md",
                        128L,
                        1
                ));

        ImportFootballDnaDataService importFootballDnaDataService = new ImportFootballDnaDataService(
                footballDnaImportTargetProvider,
                List.of(),
                List.of(footballDnaPayloadParser),
                List.of(footballDnaImportModelConverter),
                footballDnaDataArtifactRepository
        );

        ImportFootballDnaDataResult result = importFootballDnaDataService.importData();

        assertThat(result.success()).isFalse();
        assertThat(result.targetResults()).hasSize(1);
        assertThat(result.targetResults().get(0).status())
                .isEqualTo(ImportFootballDnaDataResult.TargetImportStatus.FAILED);
        assertThat(result.targetResults().get(0).failureMessage())
                .isEqualTo("No source acquirer is registered for source type 'CSV'.");
    }
}
