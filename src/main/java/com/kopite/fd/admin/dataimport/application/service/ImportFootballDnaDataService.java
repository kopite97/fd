package com.kopite.fd.admin.dataimport.application.service;

import com.kopite.fd.admin.dataimport.application.model.AcquiredImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import com.kopite.fd.admin.dataimport.application.model.ImportTargetArtifact;
import com.kopite.fd.admin.dataimport.application.model.ImportedTargetData;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportPayload;
import com.kopite.fd.admin.dataimport.application.result.ImportFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaDataArtifactRepository;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaImportModelConverter;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaImportTargetProvider;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaPayloadParser;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaSourceAcquirer;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ImportFootballDnaDataService {

    private final FootballDnaImportTargetProvider footballDnaImportTargetProvider;
    private final List<FootballDnaSourceAcquirer> footballDnaSourceAcquirers;
    private final List<FootballDnaPayloadParser> footballDnaPayloadParsers;
    private final List<FootballDnaImportModelConverter> footballDnaImportModelConverters;
    private final FootballDnaDataArtifactRepository footballDnaDataArtifactRepository;

    public ImportFootballDnaDataService(
            FootballDnaImportTargetProvider footballDnaImportTargetProvider,
            List<FootballDnaSourceAcquirer> footballDnaSourceAcquirers,
            List<FootballDnaPayloadParser> footballDnaPayloadParsers,
            List<FootballDnaImportModelConverter> footballDnaImportModelConverters,
            FootballDnaDataArtifactRepository footballDnaDataArtifactRepository
    ) {
        this.footballDnaImportTargetProvider = footballDnaImportTargetProvider;
        this.footballDnaSourceAcquirers = footballDnaSourceAcquirers;
        this.footballDnaPayloadParsers = footballDnaPayloadParsers;
        this.footballDnaImportModelConverters = footballDnaImportModelConverters;
        this.footballDnaDataArtifactRepository = footballDnaDataArtifactRepository;
    }

    public ImportFootballDnaDataResult importData() {
        LocalDateTime importedAt = LocalDateTime.now();
        List<ImportTarget> requiredTargets = footballDnaImportTargetProvider.getRequiredTargets();
        List<ImportTargetArtifact> importedTargets = new ArrayList<>();
        List<ImportFootballDnaDataResult.TargetImportResult> targetResults = new ArrayList<>();

        for (ImportTarget target : requiredTargets) {
            try {
                FootballDnaSourceAcquirer sourceAcquirer = resolveSourceAcquirer(target.sourceType());
                AcquiredImportPayload acquiredImportPayload = sourceAcquirer.acquire(target);

                FootballDnaPayloadParser payloadParser = resolvePayloadParser(acquiredImportPayload.sourceType());
                ParsedImportPayload parsedImportPayload = payloadParser.parse(acquiredImportPayload);

                FootballDnaImportModelConverter importModelConverter =
                        resolveImportModelConverter(parsedImportPayload.sourceType());
                ImportedTargetData importedTargetData = importModelConverter.convert(parsedImportPayload);

                importedTargets.add(new ImportTargetArtifact(
                        target,
                        acquiredImportPayload.rawContent(),
                        importedTargetData
                ));

                targetResults.add(new ImportFootballDnaDataResult.TargetImportResult(
                        target.targetKey(),
                        target.sourceType(),
                        target.sourceLocation(),
                        ImportFootballDnaDataResult.TargetImportStatus.SUCCEEDED,
                        importedTargetData.records().size(),
                        null
                ));
            } catch (RuntimeException exception) {
                targetResults.add(new ImportFootballDnaDataResult.TargetImportResult(
                        target.targetKey(),
                        target.sourceType(),
                        target.sourceLocation(),
                        ImportFootballDnaDataResult.TargetImportStatus.FAILED,
                        0,
                        exception.getMessage()
                ));

                FootballDnaDataArtifactRepository.SavedArtifact savedArtifact =
                        footballDnaDataArtifactRepository.save(false, importedTargets, targetResults, importedAt);

                return new ImportFootballDnaDataResult(
                        false,
                        savedArtifact.filePath(),
                        savedArtifact.byteSize(),
                        targetResults.size(),
                        importedAt,
                        List.copyOf(targetResults)
                );
            }
        }

        FootballDnaDataArtifactRepository.SavedArtifact savedArtifact =
                footballDnaDataArtifactRepository.save(true, importedTargets, targetResults, importedAt);

        return new ImportFootballDnaDataResult(
                true,
                savedArtifact.filePath(),
                savedArtifact.byteSize(),
                targetResults.size(),
                importedAt,
                List.copyOf(targetResults)
        );
    }

    private FootballDnaSourceAcquirer resolveSourceAcquirer(ImportSourceType sourceType) {
        return footballDnaSourceAcquirers.stream()
                .filter(sourceAcquirer -> sourceAcquirer.supports(sourceType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No source acquirer is registered for source type '" + sourceType + "'."
                ));
    }

    private FootballDnaPayloadParser resolvePayloadParser(ImportSourceType sourceType) {
        return footballDnaPayloadParsers.stream()
                .filter(payloadParser -> payloadParser.supports(sourceType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No payload parser is registered for source type '" + sourceType + "'."
                ));
    }

    private FootballDnaImportModelConverter resolveImportModelConverter(ImportSourceType sourceType) {
        return footballDnaImportModelConverters.stream()
                .filter(importModelConverter -> importModelConverter.supports(sourceType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No import model converter is registered for source type '" + sourceType + "'."
                ));
    }
}
