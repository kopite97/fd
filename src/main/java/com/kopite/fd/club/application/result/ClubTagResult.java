package com.kopite.fd.club.application.result;

import java.time.LocalDateTime;

public record ClubTagResult(
        Long id,
        Long clubId,
        String tagName,
        String tagType,
        Integer displayOrder,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
