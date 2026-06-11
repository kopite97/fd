package com.kopite.fd.admin.dataimport.infrastructure.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "google.sheets")
public class GoogleSheetsProperties {

    private String clubsUrl;
    private String emotionalDnaUrl;
    private String playstyleDnaUrl;
    private String clubStatusUrl;
    private String clubTagsUrl;
    private String dnaRubricUrl;
}
