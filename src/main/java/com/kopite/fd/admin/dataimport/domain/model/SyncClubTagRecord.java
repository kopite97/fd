package com.kopite.fd.admin.dataimport.domain.model;

public record SyncClubTagRecord(
        String clubCode,
        String tagName,
        String tagType,
        Integer displayOrder,
        Boolean active
) {
}
