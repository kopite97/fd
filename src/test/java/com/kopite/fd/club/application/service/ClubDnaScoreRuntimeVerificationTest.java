package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.kopite.fd.club.application.command.SaveClubCommand;
import com.kopite.fd.club.application.query.GetClubDnaScoresByClubIdQuery;
import com.kopite.fd.club.application.query.GetClubDnaScoresByDataVersionQuery;
import com.kopite.fd.club.application.result.ClubDnaScoreResult;
import com.kopite.fd.club.application.result.ClubResult;
import com.kopite.fd.club.domain.model.ClubDnaScore;
import com.kopite.fd.club.infrastructure.entity.ClubDnaScoreJpaEntity;
import com.kopite.fd.club.infrastructure.repository.ClubDnaScoreJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ClubDnaScoreRuntimeVerificationTest {

    @Autowired
    private SaveClubService saveClubService;

    @Autowired
    private GetClubDnaScoresByClubIdService getClubDnaScoresByClubIdService;

    @Autowired
    private GetClubDnaScoresByDataVersionService getClubDnaScoresByDataVersionService;

    @Autowired
    private ClubDnaScoreJpaRepository clubDnaScoreJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldRetrieveClubDnaScoresThroughRunningApplicationContext() {
        String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String uniqueCode = "DS" + suffix;
        ClubResult savedClub = saveClubService.saveClub(new SaveClubCommand(
                null,
                "Runtime DNA Score Club " + uniqueCode,
                "DS" + uniqueCode.substring(uniqueCode.length() - 4),
                uniqueCode,
                "EPL",
                "England",
                "CHALLENGER",
                "STABLE",
                new BigDecimal("2.75"),
                true,
                null,
                null,
                null
        ));
        Long dnaDefinitionId = insertDnaDefinition("EMOTIONAL", "runtime_dna_" + suffix, "Runtime DNA", 1);
        String dataVersion = "club-" + suffix.toLowerCase();
        LocalDateTime now = LocalDateTime.now();
        clubDnaScoreJpaRepository.save(ClubDnaScoreJpaEntity.fromDomain(new ClubDnaScore(
                null,
                savedClub.id(),
                dnaDefinitionId,
                new BigDecimal("4.25"),
                true,
                dataVersion,
                null,
                null,
                null,
                null,
                now,
                now
        )));

        List<ClubDnaScoreResult> clubScores = getClubDnaScoresByClubIdService.getClubDnaScoresByClubId(
                new GetClubDnaScoresByClubIdQuery(savedClub.id(), dataVersion)
        );
        List<ClubDnaScoreResult> allScores = getClubDnaScoresByDataVersionService.getClubDnaScoresByDataVersion(
                new GetClubDnaScoresByDataVersionQuery(dataVersion)
        );

        assertThat(clubScores).hasSize(1);
        assertThat(clubScores.get(0).dnaKey()).isEqualTo("runtime_dna_" + suffix);
        assertThat(clubScores.get(0).score()).isEqualByComparingTo("4.25");
        assertThat(allScores).extracting(ClubDnaScoreResult::clubId).contains(savedClub.id());
    }

    private Long insertDnaDefinition(String dnaCategory, String dnaKey, String displayName, Integer displayOrder) {
        Long dnaDefinitionId = Math.abs(UUID.randomUUID().getMostSignificantBits() % 1_000_000_000L) + 200_000_000L;
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
}
