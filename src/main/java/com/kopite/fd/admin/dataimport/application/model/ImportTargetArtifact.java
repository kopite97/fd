package com.kopite.fd.admin.dataimport.application.model;

public record ImportTargetArtifact(
        ImportTarget target,
        String rawContent,
        ImportedTargetData importedTargetData
) {
}
