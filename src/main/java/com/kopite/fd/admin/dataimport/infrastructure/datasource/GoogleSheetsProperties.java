package com.kopite.fd.admin.dataimport.infrastructure.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "google.sheets")
public class GoogleSheetsProperties {

    private String clubsUrl;
    private String emotionalDnaUrl;
    private String playstyleDnaUrl;
    private String clubStatusUrl;
    private String clubTagsUrl;
    private String dnaRubricUrl;

    public String getClubsUrl() {
        return clubsUrl;
    }

    public void setClubsUrl(String clubsUrl) {
        this.clubsUrl = clubsUrl;
    }

    public String getEmotionalDnaUrl() {
        return emotionalDnaUrl;
    }

    public void setEmotionalDnaUrl(String emotionalDnaUrl) {
        this.emotionalDnaUrl = emotionalDnaUrl;
    }

    public String getPlaystyleDnaUrl() {
        return playstyleDnaUrl;
    }

    public void setPlaystyleDnaUrl(String playstyleDnaUrl) {
        this.playstyleDnaUrl = playstyleDnaUrl;
    }

    public String getClubStatusUrl() {
        return clubStatusUrl;
    }

    public void setClubStatusUrl(String clubStatusUrl) {
        this.clubStatusUrl = clubStatusUrl;
    }

    public String getClubTagsUrl() {
        return clubTagsUrl;
    }

    public void setClubTagsUrl(String clubTagsUrl) {
        this.clubTagsUrl = clubTagsUrl;
    }

    public String getDnaRubricUrl() {
        return dnaRubricUrl;
    }

    public void setDnaRubricUrl(String dnaRubricUrl) {
        this.dnaRubricUrl = dnaRubricUrl;
    }
}
