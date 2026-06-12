package com.kopite.fd.club.domain.model;

import java.time.LocalDateTime;

public class ClubTag {

    private final Long id;
    private final Long clubId;
    private final String tagName;
    private final String tagType;
    private final Integer displayOrder;
    private final Boolean active;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ClubTag(
            Long id,
            Long clubId,
            String tagName,
            String tagType,
            Integer displayOrder,
            Boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.clubId = clubId;
        this.tagName = tagName;
        this.tagType = tagType;
        this.displayOrder = displayOrder;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getClubId() {
        return clubId;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagType() {
        return tagType;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public Boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
