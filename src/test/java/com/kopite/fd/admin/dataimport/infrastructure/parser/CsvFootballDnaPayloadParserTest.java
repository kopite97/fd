package com.kopite.fd.admin.dataimport.infrastructure.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.kopite.fd.admin.dataimport.application.model.AcquiredImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportPayload;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import org.junit.jupiter.api.Test;

class CsvFootballDnaPayloadParserTest {

    private final CsvFootballDnaPayloadParser csvFootballDnaPayloadParser = new CsvFootballDnaPayloadParser();

    @Test
    void shouldParseCsvIntoStructuredRows() {
        ParsedImportPayload result = csvFootballDnaPayloadParser.parse(new AcquiredImportPayload(
                new ImportTarget("clubs", ImportSourceType.CSV, "https://example.com/clubs.csv"),
                ImportSourceType.CSV,
                "club,name\nLIV,\"Liverpool, FC\"\nARS,Arsenal"
        ));

        assertThat(result.columnNames()).containsExactly("club", "name");
        assertThat(result.records()).hasSize(2);
        assertThat(result.records().get(0).fields()).containsEntry("name", "Liverpool, FC");
    }

    @Test
    void shouldRejectRowsWithMismatchedColumnCount() {
        assertThatThrownBy(() -> csvFootballDnaPayloadParser.parse(new AcquiredImportPayload(
                new ImportTarget("clubs", ImportSourceType.CSV, "https://example.com/clubs.csv"),
                ImportSourceType.CSV,
                "club,name\nLIV"
        ))).isInstanceOf(IllegalStateException.class)
                .hasMessage("Parsed CSV row column count does not match header count for target 'clubs'.");
    }
}
