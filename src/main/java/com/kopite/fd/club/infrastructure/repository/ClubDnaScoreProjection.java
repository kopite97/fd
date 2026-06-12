package com.kopite.fd.club.infrastructure.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ClubDnaScoreProjection {

    Long getId();

    Long getClubId();

    Long getDnaDefinitionId();

    BigDecimal getScore();

    Boolean getCore();

    String getDataVersion();

    String getDnaCategory();

    String getDnaKey();

    String getDnaDisplayName();

    Integer getDnaDisplayOrder();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
