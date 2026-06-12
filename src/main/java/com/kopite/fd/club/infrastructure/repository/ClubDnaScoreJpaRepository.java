package com.kopite.fd.club.infrastructure.repository;

import com.kopite.fd.club.infrastructure.entity.ClubDnaScoreJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClubDnaScoreJpaRepository extends JpaRepository<ClubDnaScoreJpaEntity, Long> {

    @Query(value = """
            SELECT
                cds.id AS id,
                cds.club_id AS "clubId",
                cds.dna_definition_id AS "dnaDefinitionId",
                cds.score AS score,
                cds.is_core AS core,
                cds.data_version AS "dataVersion",
                dd.dna_category AS "dnaCategory",
                dd.dna_key AS "dnaKey",
                dd.display_name AS "dnaDisplayName",
                dd.display_order AS "dnaDisplayOrder",
                cds.created_at AS "createdAt",
                cds.updated_at AS "updatedAt"
            FROM club_dna_scores cds
            JOIN dna_definitions dd ON dd.id = cds.dna_definition_id
            WHERE cds.club_id = :clubId
              AND cds.data_version = :dataVersion
              AND cds.is_deleted = false
              AND dd.is_deleted = false
            ORDER BY
                CASE dd.dna_category
                    WHEN 'EMOTIONAL' THEN 1
                    WHEN 'PLAYSTYLE' THEN 2
                    ELSE 99
                END,
                dd.display_order ASC,
                cds.id ASC
            """, nativeQuery = true)
    List<ClubDnaScoreProjection> findProjectedByClubIdAndDataVersion(
            @Param("clubId") Long clubId,
            @Param("dataVersion") String dataVersion
    );

    @Query(value = """
            SELECT
                cds.id AS id,
                cds.club_id AS "clubId",
                cds.dna_definition_id AS "dnaDefinitionId",
                cds.score AS score,
                cds.is_core AS core,
                cds.data_version AS "dataVersion",
                dd.dna_category AS "dnaCategory",
                dd.dna_key AS "dnaKey",
                dd.display_name AS "dnaDisplayName",
                dd.display_order AS "dnaDisplayOrder",
                cds.created_at AS "createdAt",
                cds.updated_at AS "updatedAt"
            FROM club_dna_scores cds
            JOIN dna_definitions dd ON dd.id = cds.dna_definition_id
            WHERE cds.data_version = :dataVersion
              AND cds.is_deleted = false
              AND dd.is_deleted = false
            ORDER BY
                cds.club_id ASC,
                CASE dd.dna_category
                    WHEN 'EMOTIONAL' THEN 1
                    WHEN 'PLAYSTYLE' THEN 2
                    ELSE 99
                END,
                dd.display_order ASC,
                cds.id ASC
            """, nativeQuery = true)
    List<ClubDnaScoreProjection> findProjectedByDataVersion(@Param("dataVersion") String dataVersion);
}
