package com.kopite.fd.admin.dataimport.application.result;

import java.util.List;

public record SyncFootballDnaDataResult(
        boolean success,
        String dataVersion,
        List<String> processedTargetKeys,
        List<TableSyncResult> tableResults,
        List<String> validationErrors,
        String failureMessage
) {

    public static SyncFootballDnaDataResult succeeded(
            String dataVersion,
            List<String> processedTargetKeys,
            List<TableSyncResult> tableResults
    ) {
        return new SyncFootballDnaDataResult(
                true,
                dataVersion,
                List.copyOf(processedTargetKeys),
                List.copyOf(tableResults),
                List.of(),
                null
        );
    }

    public static SyncFootballDnaDataResult failed(
            String dataVersion,
            List<String> processedTargetKeys,
            List<String> validationErrors,
            String failureMessage
    ) {
        return new SyncFootballDnaDataResult(
                false,
                dataVersion,
                List.copyOf(processedTargetKeys),
                List.of(),
                List.copyOf(validationErrors),
                failureMessage
        );
    }

    public record TableSyncResult(
            String tableName,
            int insertedCount,
            int updatedCount,
            int restoredCount,
            int softDeletedCount,
            int skippedCount
    ) {
    }
}
