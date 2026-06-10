package com.kopite.fd.admin.dataimport.domain.repository;

import com.kopite.fd.admin.dataimport.application.model.ImportTargetArtifact;
import com.kopite.fd.admin.dataimport.application.result.ImportFootballDnaDataResult;
import java.time.LocalDateTime;
import java.util.List;

public interface FootballDnaDataArtifactRepository {

    SavedArtifact save(
            boolean success,
            List<ImportTargetArtifact> importedTargets,
            List<ImportFootballDnaDataResult.TargetImportResult> targetResults,
            LocalDateTime importedAt
    );

    record SavedArtifact(
            String filePath,
            long byteSize,
            int targetCount
        ) {
    }
}
