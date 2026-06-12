package com.kopite.fd.club.application.query;

public record GetClubDnaScoresByClubIdQuery(
        Long clubId,
        String dataVersion
) {
}
