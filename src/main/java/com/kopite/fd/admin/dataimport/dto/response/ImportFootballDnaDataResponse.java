package com.kopite.fd.admin.dataimport.dto.response;

import com.kopite.fd.admin.dataimport.application.result.ImportFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.time.LocalDateTime;
import java.util.List;

public record ImportFootballDnaDataResponse(
        boolean success,
        String artifactPath,
        long byteSize,
        int processedTargetCount,
        LocalDateTime importedAt,
        List<TargetResultItem> targetResults
) {

    public static ImportFootballDnaDataResponse from(ImportFootballDnaDataResult result) {
        return new ImportFootballDnaDataResponse(
                result.success(),
                result.artifactPath(),
                result.byteSize(),
                result.processedTargetCount(),
                result.importedAt(),
                result.targetResults().stream()
                        .map(targetResult -> new TargetResultItem(
                                targetResult.targetKey(),
                                targetResult.sourceType(),
                                targetResult.sourceLocation(),
                                targetResult.status().name(),
                                targetResult.recordCount(),
                                targetResult.failureMessage()
                        ))
                        .toList()
        );
    }

    public record TargetResultItem(
            String targetKey,
            ImportSourceType sourceType,
            String sourceLocation,
            String status,
            int recordCount,
            String failureMessage
    ) {
    }
}
