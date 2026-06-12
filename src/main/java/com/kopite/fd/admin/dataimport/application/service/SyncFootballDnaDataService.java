package com.kopite.fd.admin.dataimport.application.service;

import com.kopite.fd.admin.dataimport.application.command.SyncFootballDnaDataCommand;
import com.kopite.fd.admin.dataimport.application.model.AcquiredImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import com.kopite.fd.admin.dataimport.application.model.ImportedRecord;
import com.kopite.fd.admin.dataimport.application.model.ImportedTargetData;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportPayload;
import com.kopite.fd.admin.dataimport.application.result.SyncFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.domain.model.DnaDefinitionSyncRef;
import com.kopite.fd.admin.dataimport.domain.model.FootballDnaDataSyncDataset;
import com.kopite.fd.admin.dataimport.domain.model.SyncClubDnaScoreRecord;
import com.kopite.fd.admin.dataimport.domain.model.SyncClubRecord;
import com.kopite.fd.admin.dataimport.domain.model.SyncClubTagRecord;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaDataSyncRepository;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaImportModelConverter;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaImportTargetProvider;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaPayloadParser;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaSourceAcquirer;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SyncFootballDnaDataService {

    private static final String CLUBS_TARGET = "clubs";
    private static final String CLUB_STATUS_TARGET = "club-status";
    private static final String CLUB_TAGS_TARGET = "club-tags";
    private static final String EMOTIONAL_DNA_TARGET = "emotional-dna";
    private static final String PLAYSTYLE_DNA_TARGET = "playstyle-dna";

    private static final BigDecimal DEFAULT_BEGINNER_ACCESSIBILITY = new BigDecimal("0.00");
    private static final BigDecimal MIN_SCORE = new BigDecimal("1.00");
    private static final BigDecimal MAX_SCORE = new BigDecimal("5.00");

    private static final List<String> EMOTIONAL_DNA_KEYS = List.of(
            "club_prestige",
            "fan_culture",
            "underdog",
            "growth",
            "star_power",
            "drama",
            "local_identity",
            "popularity"
    );
    private static final List<String> PLAYSTYLE_DNA_KEYS = List.of(
            "possession",
            "directness",
            "pressing",
            "organization",
            "creativity",
            "transition_speed"
    );

    private static final Map<String, String> COMPETITION_TIER_MAP = Map.ofEntries(
            Map.entry("DYNASTY", "DYNASTY"),
            Map.entry("DINASTY", "DYNASTY"),
            Map.entry("TITLE CONTENDER", "TITLE_CONTENDER"),
            Map.entry("TITLE_CONTENDER", "TITLE_CONTENDER"),
            Map.entry("CHALLENGER", "CHALLENGER"),
            Map.entry("MID TABLE", "MID_TABLE"),
            Map.entry("MID-TABLE", "MID_TABLE"),
            Map.entry("MID_TABLE", "MID_TABLE"),
            Map.entry("RELEGATION BATTLE", "SURVIVAL"),
            Map.entry("SURVIVAL", "SURVIVAL"),
            Map.entry("왕조", "DYNASTY"),
            Map.entry("우승후보", "TITLE_CONTENDER"),
            Map.entry("우승 후보", "TITLE_CONTENDER"),
            Map.entry("도전자", "CHALLENGER"),
            Map.entry("중위권", "MID_TABLE"),
            Map.entry("생존경쟁", "SURVIVAL"),
            Map.entry("강등권", "SURVIVAL")
    );
    private static final Map<String, String> TREND_DIRECTION_MAP = Map.ofEntries(
            Map.entry("RISING", "RISING"),
            Map.entry("STABLE", "STABLE"),
            Map.entry("REBUILDING", "REBUILDING"),
            Map.entry("DECLINING", "DECLINING"),
            Map.entry("상승세", "RISING"),
            Map.entry("안정기", "STABLE"),
            Map.entry("안정", "STABLE"),
            Map.entry("리빌딩", "REBUILDING"),
            Map.entry("하락세", "DECLINING"),
            Map.entry("침체", "DECLINING")
    );

    private final FootballDnaImportTargetProvider footballDnaImportTargetProvider;
    private final List<FootballDnaSourceAcquirer> footballDnaSourceAcquirers;
    private final List<FootballDnaPayloadParser> footballDnaPayloadParsers;
    private final List<FootballDnaImportModelConverter> footballDnaImportModelConverters;
    private final FootballDnaDataSyncRepository footballDnaDataSyncRepository;

    public SyncFootballDnaDataResult sync(SyncFootballDnaDataCommand command) {
        String dataVersion = normalize(command.dataVersion());
        List<String> validationErrors = new ArrayList<>();
        if (dataVersion == null) {
            validationErrors.add("dataVersion must not be blank.");
            return SyncFootballDnaDataResult.failed(null, List.of(), validationErrors, "Validation failed.");
        }

        LoadedSourceData loadedSourceData;
        try {
            loadedSourceData = loadSourceData();
        } catch (RuntimeException exception) {
            return SyncFootballDnaDataResult.failed(
                    dataVersion,
                    List.of(),
                    List.of(),
                    exception.getMessage()
            );
        }

        NormalizationResult normalizationResult = normalizeSourceData(dataVersion, loadedSourceData.targetsByKey());
        validationErrors.addAll(normalizationResult.validationErrors());

        if (footballDnaDataSyncRepository.existsClubDnaScoreDataVersion(dataVersion)) {
            validationErrors.add("club_dna_scores dataVersion already exists: " + dataVersion);
        }

        if (!validationErrors.isEmpty()) {
            return SyncFootballDnaDataResult.failed(
                    dataVersion,
                    loadedSourceData.processedTargetKeys(),
                    validationErrors,
                    "Validation failed."
            );
        }

        try {
            return SyncFootballDnaDataResult.succeeded(
                    dataVersion,
                    loadedSourceData.processedTargetKeys(),
                    footballDnaDataSyncRepository.synchronize(normalizationResult.dataset())
            );
        } catch (RuntimeException exception) {
            return SyncFootballDnaDataResult.failed(
                    dataVersion,
                    loadedSourceData.processedTargetKeys(),
                    List.of(),
                    exception.getMessage()
            );
        }
    }

    private LoadedSourceData loadSourceData() {
        Map<String, ImportedTargetData> targetsByKey = new LinkedHashMap<>();
        List<String> processedTargetKeys = new ArrayList<>();

        for (ImportTarget target : footballDnaImportTargetProvider.getRequiredTargets()) {
            FootballDnaSourceAcquirer sourceAcquirer = resolveSourceAcquirer(target.sourceType());
            AcquiredImportPayload acquiredImportPayload = sourceAcquirer.acquire(target);

            FootballDnaPayloadParser payloadParser = resolvePayloadParser(acquiredImportPayload.sourceType());
            ParsedImportPayload parsedImportPayload = payloadParser.parse(acquiredImportPayload);

            FootballDnaImportModelConverter importModelConverter =
                    resolveImportModelConverter(parsedImportPayload.sourceType());
            ImportedTargetData importedTargetData = importModelConverter.convert(parsedImportPayload);

            targetsByKey.put(importedTargetData.targetKey(), importedTargetData);
            processedTargetKeys.add(importedTargetData.targetKey());
        }

        return new LoadedSourceData(targetsByKey, processedTargetKeys);
    }

    private NormalizationResult normalizeSourceData(
            String dataVersion,
            Map<String, ImportedTargetData> targetsByKey
    ) {
        List<String> validationErrors = new ArrayList<>();
        validateTargetColumns(targetsByKey, validationErrors);

        ImportedTargetData clubsTarget = targetsByKey.get(CLUBS_TARGET);
        ImportedTargetData statusTarget = targetsByKey.get(CLUB_STATUS_TARGET);
        ImportedTargetData tagsTarget = targetsByKey.get(CLUB_TAGS_TARGET);
        ImportedTargetData emotionalDnaTarget = targetsByKey.get(EMOTIONAL_DNA_TARGET);
        ImportedTargetData playstyleDnaTarget = targetsByKey.get(PLAYSTYLE_DNA_TARGET);

        if (clubsTarget == null || statusTarget == null || tagsTarget == null
                || emotionalDnaTarget == null || playstyleDnaTarget == null) {
            validationErrors.add("Required synchronization targets are missing.");
            return new NormalizationResult(emptyDataset(dataVersion), validationErrors);
        }

        Map<String, ImportedRecord> clubRecordsByCode = indexUniqueClubRecords(clubsTarget, validationErrors);
        Map<String, ImportedRecord> statusRecordsByCode = indexUniqueTargetRecords(statusTarget, validationErrors);
        validateReferences(clubRecordsByCode, statusTarget, CLUB_STATUS_TARGET, validationErrors);
        validateReferences(clubRecordsByCode, tagsTarget, CLUB_TAGS_TARGET, validationErrors);
        validateReferences(clubRecordsByCode, emotionalDnaTarget, EMOTIONAL_DNA_TARGET, validationErrors);
        validateReferences(clubRecordsByCode, playstyleDnaTarget, PLAYSTYLE_DNA_TARGET, validationErrors);
        validateClubNames(clubRecordsByCode, List.of(statusTarget, tagsTarget, emotionalDnaTarget, playstyleDnaTarget),
                validationErrors);

        List<DnaDefinitionSyncRef> dnaDefinitions = footballDnaDataSyncRepository.findActiveDnaDefinitions();
        Set<DnaDefinitionIdentity> activeDnaIdentities = dnaDefinitions.stream()
                .map(definition -> new DnaDefinitionIdentity(definition.dnaCategory(), definition.dnaKey()))
                .collect(Collectors.toSet());
        validateDnaKeyCoverage(activeDnaIdentities, validationErrors);

        List<SyncClubRecord> clubs = normalizeClubs(clubRecordsByCode, statusRecordsByCode, validationErrors);
        List<SyncClubTagRecord> clubTags = normalizeTags(tagsTarget, validationErrors);
        List<SyncClubDnaScoreRecord> clubDnaScores = new ArrayList<>();
        clubDnaScores.addAll(normalizeDnaScores(
                emotionalDnaTarget,
                "EMOTIONAL",
                EMOTIONAL_DNA_KEYS,
                dataVersion,
                validationErrors
        ));
        clubDnaScores.addAll(normalizeDnaScores(
                playstyleDnaTarget,
                "PLAYSTYLE",
                PLAYSTYLE_DNA_KEYS,
                dataVersion,
                validationErrors
        ));

        return new NormalizationResult(
                new FootballDnaDataSyncDataset(dataVersion, clubs, clubTags, clubDnaScores),
                validationErrors
        );
    }

    private void validateTargetColumns(
            Map<String, ImportedTargetData> targetsByKey,
            List<String> validationErrors
    ) {
        requireColumns(targetsByKey, CLUBS_TARGET, List.of("club_id", "club_name", "league", "country", "is_active"),
                validationErrors);
        requireColumns(targetsByKey, CLUB_STATUS_TARGET, List.of("club_id", "club_name", "competition_tier", "trend"),
                validationErrors);
        requireColumns(targetsByKey, CLUB_TAGS_TARGET, List.of("club_id", "club_name", "tag", "display_order",
                "is_active"), validationErrors);
        requireColumns(targetsByKey, EMOTIONAL_DNA_TARGET, buildRequiredDnaColumns(EMOTIONAL_DNA_KEYS),
                validationErrors);
        requireColumns(targetsByKey, PLAYSTYLE_DNA_TARGET, buildRequiredDnaColumns(PLAYSTYLE_DNA_KEYS),
                validationErrors);
    }

    private List<String> buildRequiredDnaColumns(List<String> metricKeys) {
        List<String> columns = new ArrayList<>();
        columns.add("club_id");
        columns.add("club_name");
        columns.addAll(metricKeys);
        return columns;
    }

    private void requireColumns(
            Map<String, ImportedTargetData> targetsByKey,
            String targetKey,
            List<String> requiredColumns,
            List<String> validationErrors
    ) {
        ImportedTargetData target = targetsByKey.get(targetKey);
        if (target == null) {
            validationErrors.add("Missing required target: " + targetKey);
            return;
        }

        Set<String> columns = new HashSet<>(target.columnNames());
        for (String requiredColumn : requiredColumns) {
            if (!columns.contains(requiredColumn)) {
                validationErrors.add("Missing required column '" + requiredColumn + "' in target '" + targetKey + "'.");
            }
        }
    }

    private Map<String, ImportedRecord> indexUniqueClubRecords(
            ImportedTargetData target,
            List<String> validationErrors
    ) {
        Map<String, ImportedRecord> recordsByCode = new LinkedHashMap<>();
        for (ImportedRecord record : target.records()) {
            String clubCode = normalize(field(record, "club_id"));
            if (clubCode == null) {
                validationErrors.add("Blank club_id in target '" + target.targetKey() + "'.");
                continue;
            }
            if (recordsByCode.containsKey(clubCode)) {
                validationErrors.add("Duplicate club_id '" + clubCode + "' in target '" + target.targetKey() + "'.");
                continue;
            }
            recordsByCode.put(clubCode, record);
        }
        return recordsByCode;
    }

    private Map<String, ImportedRecord> indexUniqueTargetRecords(
            ImportedTargetData target,
            List<String> validationErrors
    ) {
        Map<String, ImportedRecord> recordsByCode = new LinkedHashMap<>();
        for (ImportedRecord record : target.records()) {
            String clubCode = normalize(field(record, "club_id"));
            if (clubCode == null) {
                validationErrors.add("Blank club_id in target '" + target.targetKey() + "'.");
                continue;
            }
            if (recordsByCode.containsKey(clubCode)) {
                validationErrors.add("Duplicate club_id '" + clubCode + "' in target '" + target.targetKey() + "'.");
                continue;
            }
            recordsByCode.put(clubCode, record);
        }
        return recordsByCode;
    }

    private void validateReferences(
            Map<String, ImportedRecord> clubRecordsByCode,
            ImportedTargetData target,
            String targetKey,
            List<String> validationErrors
    ) {
        for (ImportedRecord record : target.records()) {
            String clubCode = normalize(field(record, "club_id"));
            if (clubCode != null && !clubRecordsByCode.containsKey(clubCode)) {
                validationErrors.add("Target '" + targetKey + "' references unknown club_id '" + clubCode + "'.");
            }
        }
    }

    private void validateClubNames(
            Map<String, ImportedRecord> clubRecordsByCode,
            List<ImportedTargetData> targets,
            List<String> validationErrors
    ) {
        Map<String, String> clubNamesByCode = clubRecordsByCode.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> normalize(field(entry.getValue(), "club_name"))));

        for (ImportedTargetData target : targets) {
            for (ImportedRecord record : target.records()) {
                String clubCode = normalize(field(record, "club_id"));
                String targetClubName = normalize(field(record, "club_name"));
                String baseClubName = clubNamesByCode.get(clubCode);
                if (baseClubName != null && targetClubName != null && !baseClubName.equals(targetClubName)) {
                    validationErrors.add("Club name mismatch for club_id '" + clubCode + "' in target '"
                            + target.targetKey() + "'.");
                }
            }
        }
    }

    private void validateDnaKeyCoverage(
            Set<DnaDefinitionIdentity> activeDnaIdentities,
            List<String> validationErrors
    ) {
        validateDnaKeys("EMOTIONAL", EMOTIONAL_DNA_KEYS, activeDnaIdentities, validationErrors);
        validateDnaKeys("PLAYSTYLE", PLAYSTYLE_DNA_KEYS, activeDnaIdentities, validationErrors);
    }

    private void validateDnaKeys(
            String category,
            List<String> dnaKeys,
            Set<DnaDefinitionIdentity> activeDnaIdentities,
            List<String> validationErrors
    ) {
        for (String dnaKey : dnaKeys) {
            if (!activeDnaIdentities.contains(new DnaDefinitionIdentity(category, dnaKey))) {
                validationErrors.add("Missing active dna_definition for " + category + "." + dnaKey + ".");
            }
        }
    }

    private List<SyncClubRecord> normalizeClubs(
            Map<String, ImportedRecord> clubRecordsByCode,
            Map<String, ImportedRecord> statusRecordsByCode,
            List<String> validationErrors
    ) {
        List<SyncClubRecord> clubs = new ArrayList<>();

        for (Map.Entry<String, ImportedRecord> entry : clubRecordsByCode.entrySet()) {
            String clubCode = entry.getKey();
            ImportedRecord clubRecord = entry.getValue();
            ImportedRecord statusRecord = statusRecordsByCode.get(clubCode);
            if (statusRecord == null) {
                validationErrors.add("Missing club-status row for club_id '" + clubCode + "'.");
                continue;
            }

            String competitionTier = normalizeCompetitionTier(field(statusRecord, "competition_tier"));
            String trendDirection = normalizeTrendDirection(field(statusRecord, "trend"));
            if (competitionTier == null) {
                validationErrors.add("Unknown competition_tier for club_id '" + clubCode + "'.");
            }
            if (trendDirection == null) {
                validationErrors.add("Unknown trend for club_id '" + clubCode + "'.");
            }

            clubs.add(new SyncClubRecord(
                    clubCode,
                    requiredText(field(clubRecord, "club_name"), "clubs.club_name", clubCode, validationErrors),
                    clubCode,
                    requiredText(field(clubRecord, "league"), "clubs.league", clubCode, validationErrors),
                    requiredText(field(clubRecord, "country"), "clubs.country", clubCode, validationErrors),
                    competitionTier,
                    trendDirection,
                    DEFAULT_BEGINNER_ACCESSIBILITY,
                    parseBoolean(field(clubRecord, "is_active"), "clubs.is_active", clubCode, validationErrors)
            ));
        }

        return clubs;
    }

    private List<SyncClubTagRecord> normalizeTags(
            ImportedTargetData target,
            List<String> validationErrors
    ) {
        List<SyncClubTagRecord> tags = new ArrayList<>();

        for (ImportedRecord record : target.records()) {
            String clubCode = normalize(field(record, "club_id"));
            String tagName = normalizeTagName(field(record, "tag"));
            if (tagName == null) {
                validationErrors.add("Blank tag in target 'club-tags' for club_id '" + clubCode + "'.");
                continue;
            }
            tags.add(new SyncClubTagRecord(
                    clubCode,
                    tagName,
                    null,
                    parseInteger(field(record, "display_order"), "club-tags.display_order", clubCode, validationErrors),
                    parseBoolean(field(record, "is_active"), "club-tags.is_active", clubCode, validationErrors)
            ));
        }

        return tags;
    }

    private List<SyncClubDnaScoreRecord> normalizeDnaScores(
            ImportedTargetData target,
            String dnaCategory,
            List<String> dnaKeys,
            String dataVersion,
            List<String> validationErrors
    ) {
        List<SyncClubDnaScoreRecord> scores = new ArrayList<>();

        for (ImportedRecord record : target.records()) {
            String clubCode = normalize(field(record, "club_id"));
            for (String dnaKey : dnaKeys) {
                BigDecimal score = parseScore(field(record, dnaKey), target.targetKey() + "." + dnaKey, clubCode,
                        validationErrors);
                if (score == null) {
                    continue;
                }
                scores.add(new SyncClubDnaScoreRecord(clubCode, dnaCategory, dnaKey, score, false, dataVersion));
            }
        }

        return scores;
    }

    private String normalizeCompetitionTier(String value) {
        return COMPETITION_TIER_MAP.get(normalizeStatusKey(value));
    }

    private String normalizeTrendDirection(String value) {
        return TREND_DIRECTION_MAP.get(normalizeStatusKey(value));
    }

    private String normalizeStatusKey(String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            return null;
        }
        return normalized.toUpperCase(Locale.ROOT)
                .replace('_', ' ')
                .replaceAll("\\s+", " ");
    }

    private String normalizeTagName(String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            return null;
        }
        while (normalized.startsWith("#")) {
            normalized = normalized.substring(1).trim();
        }
        return normalized.isBlank() ? null : normalized;
    }

    private String requiredText(
            String value,
            String fieldName,
            String clubCode,
            List<String> validationErrors
    ) {
        String normalized = normalize(value);
        if (normalized == null) {
            validationErrors.add("Blank " + fieldName + " for club_id '" + clubCode + "'.");
        }
        return normalized;
    }

    private Boolean parseBoolean(
            String value,
            String fieldName,
            String clubCode,
            List<String> validationErrors
    ) {
        String normalized = normalize(value);
        if (normalized == null) {
            validationErrors.add("Blank " + fieldName + " for club_id '" + clubCode + "'.");
            return false;
        }
        if ("TRUE".equalsIgnoreCase(normalized)) {
            return true;
        }
        if ("FALSE".equalsIgnoreCase(normalized)) {
            return false;
        }
        validationErrors.add("Invalid boolean " + fieldName + " for club_id '" + clubCode + "'.");
        return false;
    }

    private Integer parseInteger(
            String value,
            String fieldName,
            String clubCode,
            List<String> validationErrors
    ) {
        String normalized = normalize(value);
        if (normalized == null) {
            validationErrors.add("Blank " + fieldName + " for club_id '" + clubCode + "'.");
            return 0;
        }
        try {
            return Integer.parseInt(normalized);
        } catch (NumberFormatException exception) {
            validationErrors.add("Invalid integer " + fieldName + " for club_id '" + clubCode + "'.");
            return 0;
        }
    }

    private BigDecimal parseScore(
            String value,
            String fieldName,
            String clubCode,
            List<String> validationErrors
    ) {
        String normalized = normalize(value);
        if (normalized == null) {
            validationErrors.add("Blank " + fieldName + " for club_id '" + clubCode + "'.");
            return null;
        }
        try {
            BigDecimal score = new BigDecimal(normalized).setScale(2, RoundingMode.UNNECESSARY);
            if (score.compareTo(MIN_SCORE) < 0 || score.compareTo(MAX_SCORE) > 0) {
                validationErrors.add("Score out of range " + fieldName + " for club_id '" + clubCode + "'.");
                return null;
            }
            return score;
        } catch (ArithmeticException | NumberFormatException exception) {
            validationErrors.add("Invalid score " + fieldName + " for club_id '" + clubCode + "'.");
            return null;
        }
    }

    private String field(ImportedRecord record, String fieldName) {
        return record.fields().get(fieldName);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private FootballDnaDataSyncDataset emptyDataset(String dataVersion) {
        return new FootballDnaDataSyncDataset(dataVersion, List.of(), List.of(), List.of());
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

    private record LoadedSourceData(
            Map<String, ImportedTargetData> targetsByKey,
            List<String> processedTargetKeys
    ) {
    }

    private record NormalizationResult(
            FootballDnaDataSyncDataset dataset,
            List<String> validationErrors
    ) {
    }

    private record DnaDefinitionIdentity(
            String dnaCategory,
            String dnaKey
    ) {
    }
}
