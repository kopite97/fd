package com.kopite.fd.admin.dataimport.application.model;

import java.util.Map;

public record ParsedImportRecord(
        int rowNumber,
        Map<String, String> fields
) {
}
