package com.kopite.fd.admin.dataimport.infrastructure.datasource;

import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaImportTargetProvider;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ConfiguredGoogleSheetsImportTargetProvider implements FootballDnaImportTargetProvider {

    private final GoogleSheetsProperties googleSheetsProperties;

    public ConfiguredGoogleSheetsImportTargetProvider(GoogleSheetsProperties googleSheetsProperties) {
        this.googleSheetsProperties = googleSheetsProperties;
    }

    @Override
    public List<ImportTarget> getRequiredTargets() {
        return List.of(
                requiredTarget("clubs", googleSheetsProperties.getClubsUrl()),
                requiredTarget("emotional-dna", googleSheetsProperties.getEmotionalDnaUrl()),
                requiredTarget("playstyle-dna", googleSheetsProperties.getPlaystyleDnaUrl()),
                requiredTarget("club-status", googleSheetsProperties.getClubStatusUrl()),
                requiredTarget("club-tags", googleSheetsProperties.getClubTagsUrl()),
                requiredTarget("dna-rubric", googleSheetsProperties.getDnaRubricUrl())
        );
    }

    private ImportTarget requiredTarget(String targetKey, String sourceLocation) {
        String normalizedSourceLocation = normalize(sourceLocation);
        if (normalizedSourceLocation == null) {
            throw new IllegalArgumentException("google.sheets." + targetKey + "-url must be present.");
        }

        return new ImportTarget(targetKey, ImportSourceType.CSV, normalizedSourceLocation);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
