package com.kopite.fd.admin.dataimport.infrastructure.converter;

import com.kopite.fd.admin.dataimport.application.model.ImportedRecord;
import com.kopite.fd.admin.dataimport.application.model.ImportedTargetData;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportPayload;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaImportModelConverter;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class TabularFootballDnaImportModelConverter implements FootballDnaImportModelConverter {

    @Override
    public boolean supports(ImportSourceType sourceType) {
        return sourceType == ImportSourceType.CSV || sourceType == ImportSourceType.JSON;
    }

    @Override
    public ImportedTargetData convert(ParsedImportPayload payload) {
        List<ImportedRecord> importedRecords = payload.records().stream()
                .map(record -> new ImportedRecord(Map.copyOf(record.fields())))
                .toList();

        return new ImportedTargetData(
                payload.target().targetKey(),
                payload.sourceType(),
                payload.target().sourceLocation(),
                List.copyOf(payload.columnNames()),
                importedRecords
        );
    }
}
