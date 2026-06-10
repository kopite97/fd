package com.kopite.fd.admin.dataimport.infrastructure.parser;

import com.kopite.fd.admin.dataimport.application.model.AcquiredImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ParsedImportRecord;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaPayloadParser;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CsvFootballDnaPayloadParser implements FootballDnaPayloadParser {

    @Override
    public boolean supports(ImportSourceType sourceType) {
        return sourceType == ImportSourceType.CSV;
    }

    @Override
    public ParsedImportPayload parse(AcquiredImportPayload payload) {
        List<List<String>> rows = parseRows(payload.rawContent());
        if (rows.isEmpty()) {
            throw new IllegalStateException("Imported CSV content is empty.");
        }

        List<String> headers = rows.get(0);
        if (headers.isEmpty()) {
            throw new IllegalStateException("Imported CSV headers are empty.");
        }

        List<ParsedImportRecord> records = new ArrayList<>();
        for (int index = 1; index < rows.size(); index++) {
            List<String> row = rows.get(index);
            if (isBlankRow(row)) {
                continue;
            }

            if (row.size() != headers.size()) {
                throw new IllegalStateException(
                        "Parsed CSV row column count does not match header count for target '"
                                + payload.target().targetKey()
                                + "'."
                );
            }

            Map<String, String> fields = new LinkedHashMap<>();
            for (int columnIndex = 0; columnIndex < headers.size(); columnIndex++) {
                fields.put(headers.get(columnIndex), row.get(columnIndex));
            }

            records.add(new ParsedImportRecord(index + 1, fields));
        }

        return new ParsedImportPayload(
                payload.target(),
                payload.sourceType(),
                List.copyOf(headers),
                List.copyOf(records)
        );
    }

    private List<List<String>> parseRows(String csvContent) {
        List<List<String>> rows = new ArrayList<>();
        List<String> currentRow = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (int index = 0; index < csvContent.length(); index++) {
            char currentChar = csvContent.charAt(index);

            if (currentChar == '"') {
                if (inQuotes && index + 1 < csvContent.length() && csvContent.charAt(index + 1) == '"') {
                    currentField.append('"');
                    index++;
                } else {
                    inQuotes = !inQuotes;
                }
                continue;
            }

            if (currentChar == ',' && !inQuotes) {
                currentRow.add(currentField.toString());
                currentField.setLength(0);
                continue;
            }

            if ((currentChar == '\n' || currentChar == '\r') && !inQuotes) {
                if (currentChar == '\r' && index + 1 < csvContent.length() && csvContent.charAt(index + 1) == '\n') {
                    index++;
                }

                currentRow.add(currentField.toString());
                currentField.setLength(0);
                rows.add(currentRow);
                currentRow = new ArrayList<>();
                continue;
            }

            currentField.append(currentChar);
        }

        currentRow.add(currentField.toString());
        if (!isSingleEmptyFieldRow(currentRow)) {
            rows.add(currentRow);
        }

        return rows;
    }

    private boolean isBlankRow(List<String> row) {
        for (String value : row) {
            if (value != null && !value.isBlank()) {
                return false;
            }
        }
        return true;
    }

    private boolean isSingleEmptyFieldRow(List<String> row) {
        return row.size() == 1 && row.get(0).isEmpty();
    }
}
