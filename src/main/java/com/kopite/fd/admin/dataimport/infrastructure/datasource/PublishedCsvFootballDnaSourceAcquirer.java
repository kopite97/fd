package com.kopite.fd.admin.dataimport.infrastructure.datasource;

import com.kopite.fd.admin.dataimport.application.model.AcquiredImportPayload;
import com.kopite.fd.admin.dataimport.application.model.ImportTarget;
import com.kopite.fd.admin.dataimport.domain.repository.FootballDnaSourceAcquirer;
import com.kopite.fd.admin.dataimport.domain.type.ImportSourceType;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.stereotype.Component;

@Component
public class PublishedCsvFootballDnaSourceAcquirer implements FootballDnaSourceAcquirer {

    private final HttpClient httpClient;

    public PublishedCsvFootballDnaSourceAcquirer() {
        this(HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build());
    }

    PublishedCsvFootballDnaSourceAcquirer(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public boolean supports(ImportSourceType sourceType) {
        return sourceType == ImportSourceType.CSV;
    }

    @Override
    public AcquiredImportPayload acquire(ImportTarget target) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(target.sourceLocation()))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (responseBody == null) {
                throw new IllegalStateException("Published CSV response body is empty.");
            }

            return new AcquiredImportPayload(target, target.sourceType(), responseBody);
        } catch (IOException | InterruptedException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new IllegalStateException(
                    "Failed to fetch published CSV for target '" + target.targetKey() + "'.",
                    exception
            );
        }
    }
}
