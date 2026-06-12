package com.kopite.fd.club.infrastructure.entity;

import com.kopite.fd.club.domain.model.ClubTag;
import com.kopite.fd.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "club_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubTagJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Column(name = "tag_name", nullable = false, length = 100)
    private String tagName;

    @Column(name = "tag_type", nullable = true, length = 30)
    private String tagType;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    private ClubTagJpaEntity(
            Long id,
            Long clubId,
            String tagName,
            String tagType,
            Integer displayOrder,
            Boolean active,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        super(createdAt, updatedAt, false);
        this.id = id;
        this.clubId = clubId;
        this.tagName = tagName;
        this.tagType = tagType;
        this.displayOrder = displayOrder;
        this.active = active;
    }

    public static ClubTagJpaEntity fromDomain(ClubTag clubTag) {
        return new ClubTagJpaEntity(
                clubTag.getId(),
                clubTag.getClubId(),
                clubTag.getTagName(),
                clubTag.getTagType(),
                clubTag.getDisplayOrder(),
                clubTag.isActive(),
                clubTag.getCreatedAt(),
                clubTag.getUpdatedAt()
        );
    }

    public static ClubTagJpaEntity createForSync(
            Long clubId,
            String tagName,
            String tagType,
            Integer displayOrder,
            Boolean active
    ) {
        LocalDateTime now = LocalDateTime.now();
        return new ClubTagJpaEntity(
                null,
                clubId,
                tagName,
                tagType,
                displayOrder,
                active,
                now,
                now
        );
    }

    public void updateFromSync(String tagType, Integer displayOrder, Boolean active) {
        this.tagType = tagType;
        this.displayOrder = displayOrder;
        this.active = active;
        restore();
    }

    public void markSoftDeleted() {
        markDeleted();
    }

    public ClubTag toDomain() {
        return new ClubTag(
                id,
                clubId,
                tagName,
                tagType,
                displayOrder,
                active,
                getCreatedAt(),
                getUpdatedAt()
        );
    }
}
