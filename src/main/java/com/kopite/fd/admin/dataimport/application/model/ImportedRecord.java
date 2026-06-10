package com.kopite.fd.admin.dataimport.application.model;

import java.util.Map;

public record ImportedRecord(
        Map<String, String> fields
) {
}
