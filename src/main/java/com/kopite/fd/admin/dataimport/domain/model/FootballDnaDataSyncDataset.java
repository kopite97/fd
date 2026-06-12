package com.kopite.fd.admin.dataimport.domain.model;

import java.util.List;

public record FootballDnaDataSyncDataset(
        String dataVersion,
        List<SyncClubRecord> clubs,
        List<SyncClubTagRecord> clubTags,
        List<SyncClubDnaScoreRecord> clubDnaScores
) {
}
