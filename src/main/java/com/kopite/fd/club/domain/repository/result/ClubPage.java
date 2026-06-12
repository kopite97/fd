package com.kopite.fd.club.domain.repository.result;

import com.kopite.fd.club.domain.model.Club;
import java.util.List;

public record ClubPage(
        List<Club> clubs,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
}
