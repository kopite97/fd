package com.kopite.fd.admin.dataimport.application.model;

import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;

public record ImportTarget(
        String targetKey,
        ImportSourceType sourceType,
        String sourceLocation
) {
}
