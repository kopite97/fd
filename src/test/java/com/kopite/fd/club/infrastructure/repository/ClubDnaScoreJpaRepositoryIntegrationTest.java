package com.kopite.fd.club.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.domain.model.ClubDnaScore;
import com.kopite.fd.club.infrastructure.entity.ClubDnaScoreJpaEntity;
import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=update")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClubDnaScoreJpaRepositoryIntegrationTest {

    @Autowired
    private ClubJpaRepository clubJpaRepository;

    @Autowired
    private ClubDnaScoreJpaRepository clubDnaScoreJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldFindClubDnaScoresByClubIdAndDataVersionWithMetadataAndDeterministicOrder() {
        String suffix = uniqueSuffix();
        Long clubId = saveClub("DNA" + suffix);
        Long emotionalDnaId = insertDnaDefinition("EMOTIONAL", "fan_culture_" + suffix, "Fan Culture", 2);
        Long playstyleDnaId = insertDnaDefinition("PLAYSTYLE", "pressing_" + suffix, "Pressing", 1);
        String dataVersion = "club-" + suffix.toLowerCase();
        LocalDateTime now = LocalDateTime.now();

        ClubDnaScoreJpaEntity playstyleScore = clubDnaScoreJpaRepository.save(ClubDnaScoreJpaEntity.fromDomain(new ClubDnaScore(
                null, clubId, playstyleDnaId, new BigDecimal("3.50"), false, dataVersion,
                null, null, null, null, now, now
        )));
        ClubDnaScoreJpaEntity emotionalScore = clubDnaScoreJpaRepository.save(ClubDnaScoreJpaEntity.fromDomain(new ClubDnaScore(
                null, clubId, emotionalDnaId, new BigDecimal("4.50"), true, dataVersion,
                null, null, null, null, now, now
        )));
        clubDnaScoreJpaRepository.save(ClubDnaScoreJpaEntity.fromDomain(new ClubDnaScore(
                null, clubId, emotionalDnaId, new BigDecimal("2.50"), false, "other-" + dataVersion,
                null, null, null, null, now, now
        )));
        insertDeletedClubDnaScore(clubId, emotionalDnaId, dataVersion);

        List<ClubDnaScoreProjection> results = clubDnaScoreJpaRepository.findProjectedByClubIdAndDataVersion(
                clubId,
                dataVersion
        );

        assertThat(results).extracting(ClubDnaScoreProjection::getId)
                .containsExactly(emotionalScore.getId(), playstyleScore.getId());
        assertThat(results.get(0).getDnaCategory()).isEqualTo("EMOTIONAL");
        assertThat(results.get(0).getDnaKey()).isEqualTo("fan_culture_" + suffix);
        assertThat(results.get(0).getScore()).isEqualByComparingTo("4.50");
        assertThat(results.get(0).getCore()).isTrue();
    }

    @Test
    void shouldFindAllClubDnaScoresByDataVersionWithClubFirstOrdering() {
        String suffix = uniqueSuffix();
        Long firstClubId = saveClub("DNA" + suffix + "A");
        Long secondClubId = saveClub("DNA" + suffix + "B");
        Long emotionalDnaId = insertDnaDefinition("EMOTIONAL", "growth_" + suffix, "Growth", 1);
        String dataVersion = "club-" + suffix.toLowerCase();
        LocalDateTime now = LocalDateTime.now();

        ClubDnaScoreJpaEntity secondClubScore = clubDnaScoreJpaRepository.save(ClubDnaScoreJpaEntity.fromDomain(new ClubDnaScore(
                null, secondClubId, emotionalDnaId, new BigDecimal("3.50"), false, dataVersion,
                null, null, null, null, now, now
        )));
        ClubDnaScoreJpaEntity firstClubScore = clubDnaScoreJpaRepository.save(ClubDnaScoreJpaEntity.fromDomain(new ClubDnaScore(
                null, firstClubId, emotionalDnaId, new BigDecimal("4.50"), true, dataVersion,
                null, null, null, null, now, now
        )));

        List<ClubDnaScoreProjection> results = clubDnaScoreJpaRepository.findProjectedByDataVersion(dataVersion);

        assertThat(results).extracting(ClubDnaScoreProjection::getId)
                .containsExactly(firstClubScore.getId(), secondClubScore.getId());
    }

    private Long saveClub(String code) {
        Club club = new Club(
                null,
                "Club " + code,
                "C" + code.substring(code.length() - 4),
                code,
                "EPL",
                "England",
                "CHALLENGER",
                "STABLE",
                new BigDecimal("3.50"),
                true,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return clubJpaRepository.save(ClubJpaEntity.fromDomain(club)).getId();
    }

    private Long insertDnaDefinition(String dnaCategory, String dnaKey, String displayName, Integer displayOrder) {
        Long dnaDefinitionId = nextDnaDefinitionId();
        jdbcTemplate.update("""
                        INSERT INTO dna_definitions
                            (id, dna_category, dna_key, display_name, description, display_order, is_active, created_at, updated_at, is_deleted)
                        VALUES
                            (?, ?, ?, ?, null, ?, true, now(), now(), false)
                        """,
                dnaDefinitionId,
                dnaCategory,
                dnaKey,
                displayName,
                displayOrder
        );
        return dnaDefinitionId;
    }

    private void insertDeletedClubDnaScore(Long clubId, Long dnaDefinitionId, String dataVersion) {
        jdbcTemplate.update("""
                        INSERT INTO club_dna_scores
                            (club_id, dna_definition_id, score, is_core, data_version, created_at, updated_at, is_deleted)
                        VALUES
                            (?, ?, 4.00, false, ?, now(), now(), true)
                        """,
                clubId,
                dnaDefinitionId,
                dataVersion
        );
    }

    private Long nextDnaDefinitionId() {
        return Math.abs(UUID.randomUUID().getMostSignificantBits() % 1_000_000_000L) + 100_000_000L;
    }

    private String uniqueSuffix() {
        return UUID.randomUUID().toString().substring(0, 8).replace("-", "").toUpperCase();
    }
}
