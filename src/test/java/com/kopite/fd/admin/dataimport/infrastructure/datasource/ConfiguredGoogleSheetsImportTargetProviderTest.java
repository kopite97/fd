package com.kopite.fd.admin.dataimport.infrastructure.datasource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConfiguredGoogleSheetsImportTargetProviderTest {

    @Test
    void shouldProvideExplicitlyConfiguredTargetsInStableOrder() {
        GoogleSheetsProperties properties = new GoogleSheetsProperties();
        properties.setClubsUrl("https://example.com/clubs.csv");
        properties.setEmotionalDnaUrl("https://example.com/emotional.csv");
        properties.setPlaystyleDnaUrl("https://example.com/playstyle.csv");
        properties.setClubStatusUrl("https://example.com/status.csv");
        properties.setClubTagsUrl("https://example.com/tags.csv");
        properties.setDnaRubricUrl("https://example.com/rubric.csv");

        List<ImportTarget> targets = new ConfiguredGoogleSheetsImportTargetProvider(properties).getRequiredTargets();

        assertThat(targets)
                .extracting(ImportTarget::targetKey)
                .containsExactly(
                        "clubs",
                        "emotional-dna",
                        "playstyle-dna",
                        "club-status",
                        "club-tags",
                        "dna-rubric"
                );
    }

    @Test
    void shouldRejectMissingTargetConfiguration() {
        GoogleSheetsProperties properties = new GoogleSheetsProperties();
        properties.setClubsUrl("https://example.com/clubs.csv");

        assertThatThrownBy(() -> new ConfiguredGoogleSheetsImportTargetProvider(properties).getRequiredTargets())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("google.sheets.emotional-dna-url must be present.");
    }
}
