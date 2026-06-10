package com.kopite.fd.admin.dataimport.application.model;

import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.util.List;

public record ImportedTargetData(
        String targetKey,
        ImportSourceType sourceType,
        String sourceLocation,
        List<String> columnNames,
        List<ImportedRecord> records
) {
}
