package com.kopite.fd.admin.dataimport.infrastructure.artifact;

import com.kopite.fd.admin.dataimport.application.model.ImportTargetArtifact;
import com.kopite.fd.admin.dataimport.application.model.ImportedRecord;
import com.kopite.fd.admin.dataimport.application.result.ImportFootballDnaDataResult;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaDataArtifactRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MarkdownFootballDnaDataArtifactRepository implements FootballDnaDataArtifactRepository {

    private static final Path OUTPUT_DIRECTORY = Path.of("src", "main", "resources", "data");
    private static final DateTimeFormatter FILE_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    @Override
    public SavedArtifact save(
            boolean success,
            List<ImportTargetArtifact> importedTargets,
            List<ImportFootballDnaDataResult.TargetImportResult> targetResults,
            LocalDateTime importedAt
    ) {
        try {
            Files.createDirectories(OUTPUT_DIRECTORY);

            String fileName = "football-dna-data-import-" + importedAt.format(FILE_TIMESTAMP_FORMAT) + ".md";
            Path artifactPath = OUTPUT_DIRECTORY.resolve(fileName);
            String markdown = buildMarkdown(success, importedTargets, targetResults, importedAt);

            Files.writeString(artifactPath, markdown, StandardCharsets.UTF_8);

            return new SavedArtifact(
                    artifactPath.toString().replace('\\', '/'),
                    Files.size(artifactPath),
                    targetResults.size()
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to write imported CSV artifact.", exception);
        }
    }

    private String buildMarkdown(
            boolean success,
            List<ImportTargetArtifact> importedTargets,
            List<ImportFootballDnaDataResult.TargetImportResult> targetResults,
            LocalDateTime importedAt
    ) {
        StringBuilder markdown = new StringBuilder();
        markdown.append("# Football DNA Data Import\n\n");
        markdown.append("- Imported At: ").append(importedAt).append('\n');
        markdown.append("- Overall Success: ").append(success).append('\n');
        markdown.append("- Artifact Strategy: single aggregated artifact\n\n");
        markdown.append("## Target Results\n\n");

        for (ImportFootballDnaDataResult.TargetImportResult targetResult : targetResults) {
            markdown.append("- `").append(targetResult.targetKey()).append("`")
                    .append(" status=").append(targetResult.status())
                    .append(", sourceType=").append(targetResult.sourceType())
                    .append(", records=").append(targetResult.recordCount());

            if (targetResult.failureMessage() != null) {
                markdown.append(", failure=").append(targetResult.failureMessage());
            }

            markdown.append('\n');
        }

        for (ImportTargetArtifact importedTarget : importedTargets) {
            markdown.append("\n## Target: ").append(importedTarget.target().targetKey()).append("\n\n");
            markdown.append("- Source Type: ").append(importedTarget.target().sourceType()).append('\n');
            markdown.append("- Source Location: ").append(importedTarget.target().sourceLocation()).append('\n');
            markdown.append("- Columns: ")
                    .append(String.join(", ", importedTarget.importedTargetData().columnNames()))
                    .append('\n');
            markdown.append("- Record Count: ")
                    .append(importedTarget.importedTargetData().records().size())
                    .append("\n\n");
            markdown.append("### Raw Payload\n\n");
            markdown.append("```text\n")
                    .append(importedTarget.rawContent())
                    .append(ensureTrailingNewline(importedTarget.rawContent()))
                    .append("```\n\n");
            markdown.append("### Imported Record Preview\n\n");
            markdown.append("```text\n")
                    .append(buildRecordPreview(importedTarget.importedTargetData().records()))
                    .append("```\n");
        }

        return markdown.toString();
    }

    private String buildRecordPreview(List<ImportedRecord> records) {
        if (records.isEmpty()) {
            return "(no records)\n";
        }

        return records.stream()
                .limit(3)
                .map(record -> record.fields().entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .collect(Collectors.joining(", ")))
                .collect(Collectors.joining("\n", "", "\n"));
    }

    private String ensureTrailingNewline(String value) {
        return value.endsWith("\n") ? "" : "\n";
    }
}
