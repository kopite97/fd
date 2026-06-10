package com.kopite.fd.admin.dataimport.application.result;

import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.time.LocalDateTime;
import java.util.List;

public record ImportFootballDnaDataResult(
        boolean success,
        String artifactPath,
        long byteSize,
        int processedTargetCount,
        LocalDateTime importedAt,
        List<TargetImportResult> targetResults
) {

    public record TargetImportResult(
            String targetKey,
            ImportSourceType sourceType,
            String sourceLocation,
            TargetImportStatus status,
            int recordCount,
            String failureMessage
    ) {
    }

    public enum TargetImportStatus {
        SUCCEEDED,
        FAILED
    }
}
