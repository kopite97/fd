package com.kopite.fd.admin.dataimport.infrastructure.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import com.kopite.fd.admin.dataimport.application.model.ImportedTargetData;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportRecord;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class TabularFootballDnaImportModelConverterTest {

    private final TabularFootballDnaImportModelConverter tabularFootballDnaImportModelConverter =
            new TabularFootballDnaImportModelConverter();

    @Test
    void shouldConvertParsedPayloadIntoApplicationLevelImportModel() {
        ImportedTargetData result = tabularFootballDnaImportModelConverter.convert(new ParsedImportPayload(
                new ImportTarget("clubs", ImportSourceType.CSV, "https://example.com/clubs.csv"),
                ImportSourceType.CSV,
                List.of("club", "name"),
                List.of(new ParsedImportRecord(2, Map.of("club", "LIV", "name", "Liverpool")))
        ));

        assertThat(result.targetKey()).isEqualTo("clubs");
        assertThat(result.columnNames()).containsExactly("club", "name");
        assertThat(result.records()).hasSize(1);
        assertThat(result.records().get(0).fields()).containsEntry("name", "Liverpool");
    }
}
