package com.kopite.fd.club.application.result;

import java.util.List;

public record ClubPageResult(
        List<ClubResult> clubs,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
