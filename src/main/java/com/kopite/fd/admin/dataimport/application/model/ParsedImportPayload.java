package com.kopite.fd.admin.dataimport.application.model;

import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.util.List;

public record ParsedImportPayload(
        ImportTarget target,
        ImportSourceType sourceType,
        List<String> columnNames,
        List<ParsedImportRecord> records
) {
}
