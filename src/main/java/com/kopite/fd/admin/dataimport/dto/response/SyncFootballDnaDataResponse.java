package com.kopite.fd.admin.dataimport.dto.response;

import com.kopite.fd.admin.dataimport.application.result.SyncFootballDnaDataResult;
import java.util.List;

public record SyncFootballDnaDataResponse(
        boolean success,
        String dataVersion,
        List<String> processedTargetKeys,
        List<TableSyncResponse> tableResults,
        List<String> validationErrors,
        String failureMessage
) {

    public static SyncFootballDnaDataResponse from(SyncFootballDnaDataResult result) {
        return new SyncFootballDnaDataResponse(
                result.success(),
                result.dataVersion(),
                result.processedTargetKeys(),
                result.tableResults().stream()
                        .map(TableSyncResponse::from)
                        .toList(),
                result.validationErrors(),
                result.failureMessage()
        );
    }

    public record TableSyncResponse(
            String tableName,
            int insertedCount,
            int updatedCount,
            int restoredCount,
            int softDeletedCount,
            int skippedCount
    ) {

        private static TableSyncResponse from(SyncFootballDnaDataResult.TableSyncResult result) {
            return new TableSyncResponse(
                    result.tableName(),
                    result.insertedCount(),
                    result.updatedCount(),
                    result.restoredCount(),
                    result.softDeletedCount(),
                    result.skippedCount()
            );
        }
    }
}
