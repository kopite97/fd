package com.kopite.fd.admin.dataimport.application.model;

import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;

public record AcquiredImportPayload(
        ImportTarget target,
        ImportSourceType sourceType,
        String rawContent
) {
}
