package com.kopite.fd.club.infrastructure.entity;

import com.kopite.fd.club.domain.model.ClubDnaScore;
import com.kopite.fd.global.infrastructure.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "club_dna_scores")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubDnaScoreJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "club_id", nullable = false)
    private Long clubId;

    @Column(name = "dna_definition_id", nullable = false)
    private Long dnaDefinitionId;

    @Column(name = "score", nullable = false, precision = 5, scale = 2)
    private BigDecimal score;

    @Column(name = "is_core", nullable = false)
    private Boolean core;

    @Column(name = "data_version", nullable = false, length = 20)
    private String dataVersion;

    private ClubDnaScoreJpaEntity(
            Long id,
            Long clubId,
            Long dnaDefinitionId,
            BigDecimal score,
            Boolean core,
            String dataVersion,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        super(createdAt, updatedAt, false);
        this.id = id;
        this.clubId = clubId;
        this.dnaDefinitionId = dnaDefinitionId;
        this.score = score;
        this.core = core;
        this.dataVersion = dataVersion;
    }

    public static ClubDnaScoreJpaEntity fromDomain(ClubDnaScore clubDnaScore) {
        return new ClubDnaScoreJpaEntity(
                clubDnaScore.getId(),
                clubDnaScore.getClubId(),
                clubDnaScore.getDnaDefinitionId(),
                clubDnaScore.getScore(),
                clubDnaScore.isCore(),
                clubDnaScore.getDataVersion(),
                clubDnaScore.getCreatedAt(),
                clubDnaScore.getUpdatedAt()
        );
    }

    public ClubDnaScore toDomain() {
        return new ClubDnaScore(
                id,
                clubId,
                dnaDefinitionId,
                score,
                core,
                dataVersion,
                null,
                null,
                null,
                null,
                getCreatedAt(),
                getUpdatedAt()
        );
    }
}
