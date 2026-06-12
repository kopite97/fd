package com.kopite.fd.admin.dataimport.infrastructure.adapter;

import static org.assertj.core.api.Assertions.assertThat;

import com.kopite.fd.admin.dataimport.application.result.SyncFootballDnaDataResult.TableSyncResult;
import com.kopite.fd.admin.dataimport.domain.model.FootballDnaDataSyncDataset;
import com.kopite.fd.admin.dataimport.domain.model.SyncClubDnaScoreRecord;
import com.kopite.fd.admin.dataimport.domain.model.SyncClubRecord;
import com.kopite.fd.admin.dataimport.domain.model.SyncClubTagRecord;
import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import com.kopite.fd.club.infrastructure.entity.ClubTagJpaEntity;
import com.kopite.fd.club.infrastructure.repository.ClubDnaScoreJpaRepository;
import com.kopite.fd.club.infrastructure.repository.ClubJpaRepository;
import com.kopite.fd.club.infrastructure.repository.ClubTagJpaRepository;
import com.kopite.fd.dna.infrastructure.repository.DnaDefinitionJpaRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=update")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(FootballDnaDataSyncPersistenceAdapter.class)
class FootballDnaDataSyncPersistenceAdapterIntegrationTest {

    @Autowired
    private FootballDnaDataSyncPersistenceAdapter adapter;

    @Autowired
    private ClubJpaRepository clubJpaRepository;

    @Autowired
    private ClubTagJpaRepository clubTagJpaRepository;

    @Autowired
    private ClubDnaScoreJpaRepository clubDnaScoreJpaRepository;

    @Autowired
    private DnaDefinitionJpaRepository dnaDefinitionJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldSynchronizeTablesAndPersistRequestedDataVersion() {
        String suffix = uniqueSuffix();
        String firstCode = "SYA" + suffix;
        String secondCode = "SYB" + suffix;
        String dataVersion = "club-" + suffix.toLowerCase();
        Long prestigeId = insertDnaDefinition("EMOTIONAL", "club_prestige_" + suffix);
        Long pressingId = insertDnaDefinition("PLAYSTYLE", "pressing_" + suffix);

        FootballDnaDataSyncDataset dataset = new FootballDnaDataSyncDataset(
                dataVersion,
                List.of(club(firstCode), club(secondCode)),
                List.of(
                        tag(firstCode, "명문클럽", 1),
                        tag(secondCode, "언더독", 1)
                ),
                List.of(
                        score(firstCode, "EMOTIONAL", "club_prestige_" + suffix, "5.00", dataVersion),
                        score(secondCode, "PLAYSTYLE", "pressing_" + suffix, "3.00", dataVersion)
                )
        );

        List<TableSyncResult> results = adapter.synchronize(dataset);

        assertThat(results).extracting(TableSyncResult::tableName)
                .containsExactly("clubs", "club_tags", "club_dna_scores");
        assertThat(count("clubs", "code in (?, ?)", firstCode, secondCode)).isEqualTo(2);
        assertThat(count("club_tags", "club_id in (select id from clubs where code in (?, ?))", firstCode, secondCode))
                .isEqualTo(2);
        assertThat(count("club_dna_scores", "data_version = ?", dataVersion)).isEqualTo(2);
        assertThat(clubDnaScoreJpaRepository.existsByDataVersion(dataVersion)).isTrue();
        assertThat(dnaDefinitionJpaRepository.findById(prestigeId)).isPresent();
        assertThat(dnaDefinitionJpaRepository.findById(pressingId)).isPresent();
    }

    @Test
    void shouldRestoreSoftDeletedTagOnlyWhenClubIdAndNormalizedTagNameMatch() {
        String suffix = uniqueSuffix();
        String firstCode = "RTA" + suffix;
        String secondCode = "RTB" + suffix;
        String dataVersion = "club-" + suffix.toLowerCase();
        insertDnaDefinition("EMOTIONAL", "club_prestige_" + suffix);

        ClubJpaEntity firstClub = clubJpaRepository.save(ClubJpaEntity.createForSync(
                "Club " + firstCode,
                firstCode,
                firstCode,
                "EPL",
                "England",
                "CHALLENGER",
                "STABLE",
                new BigDecimal("0.00"),
                true
        ));
        ClubJpaEntity secondClub = clubJpaRepository.save(ClubJpaEntity.createForSync(
                "Club " + secondCode,
                secondCode,
                secondCode,
                "EPL",
                "England",
                "CHALLENGER",
                "STABLE",
                new BigDecimal("0.00"),
                true
        ));

        ClubTagJpaEntity firstDeletedTag = ClubTagJpaEntity.createForSync(firstClub.getId(), "Legacy", null, 9, true);
        firstDeletedTag.markSoftDeleted();
        clubTagJpaRepository.save(firstDeletedTag);
        ClubTagJpaEntity secondActiveTag = clubTagJpaRepository.save(
                ClubTagJpaEntity.createForSync(secondClub.getId(), "Legacy", null, 1, true)
        );

        FootballDnaDataSyncDataset dataset = new FootballDnaDataSyncDataset(
                dataVersion,
                List.of(club(firstCode), club(secondCode)),
                List.of(tag(firstCode, "Legacy", 2)),
                List.of(score(firstCode, "EMOTIONAL", "club_prestige_" + suffix, "4.00", dataVersion))
        );

        List<TableSyncResult> results = adapter.synchronize(dataset);

        List<ClubTagJpaEntity> tags = clubTagJpaRepository.findByClubIdIn(List.of(firstClub.getId(), secondClub.getId()));
        ClubTagJpaEntity restoredFirstTag = tags.stream()
                .filter(tag -> tag.getId().equals(firstDeletedTag.getId()))
                .findFirst()
                .orElseThrow();
        ClubTagJpaEntity softDeletedSecondTag = tags.stream()
                .filter(tag -> tag.getId().equals(secondActiveTag.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(results.get(1).restoredCount()).isEqualTo(1);
        assertThat(results.get(1).softDeletedCount()).isEqualTo(1);
        assertThat(restoredFirstTag.isDeleted()).isFalse();
        assertThat(restoredFirstTag.getDisplayOrder()).isEqualTo(2);
        assertThat(softDeletedSecondTag.isDeleted()).isTrue();
    }

    private SyncClubRecord club(String code) {
        return new SyncClubRecord(
                code,
                "Club " + code,
                code,
                "EPL",
                "England",
                "CHALLENGER",
                "STABLE",
                new BigDecimal("0.00"),
                true
        );
    }

    private SyncClubTagRecord tag(String clubCode, String tagName, Integer displayOrder) {
        return new SyncClubTagRecord(clubCode, tagName, null, displayOrder, true);
    }

    private SyncClubDnaScoreRecord score(
            String clubCode,
            String dnaCategory,
            String dnaKey,
            String score,
            String dataVersion
    ) {
        return new SyncClubDnaScoreRecord(clubCode, dnaCategory, dnaKey, new BigDecimal(score), false, dataVersion);
    }

    private Long insertDnaDefinition(String category, String dnaKey) {
        Long dnaDefinitionId = Math.abs(UUID.randomUUID().getMostSignificantBits() % 1_000_000_000L) + 200_000_000L;
        jdbcTemplate.update("""
                        INSERT INTO dna_definitions
                            (id, dna_category, dna_key, display_name, description, display_order, is_active, created_at, updated_at, is_deleted)
                        VALUES
                            (?, ?, ?, ?, null, 1, true, now(), now(), false)
                        """,
                dnaDefinitionId,
                category,
                dnaKey,
                dnaKey
        );
        return dnaDefinitionId;
    }

    private Integer count(String tableName, String whereClause, Object... args) {
        return jdbcTemplate.queryForObject(
                "select count(*) from " + tableName + " where " + whereClause,
                Integer.class,
                args
        );
    }

    private String uniqueSuffix() {
        return UUID.randomUUID().toString().substring(0, 6).replace("-", "").toUpperCase();
    }
}
