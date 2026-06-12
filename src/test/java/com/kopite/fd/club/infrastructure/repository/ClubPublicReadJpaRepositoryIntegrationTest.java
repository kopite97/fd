package com.kopite.fd.club.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=update")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClubPublicReadJpaRepositoryIntegrationTest {

    @Autowired
    private ClubJpaRepository clubJpaRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldFindOnlyActiveNonDeletedClubsInIdOrder() {
        String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Long visibleClubId = insertClub("PUBA" + suffix, true, false);
        Long inactiveClubId = insertClub("PUBI" + suffix, false, false);
        Long deletedClubId = insertClub("PUBD" + suffix, true, true);

        Page<ClubJpaEntity> result = clubJpaRepository.findByActiveTrueAndIsDeletedFalse(
                PageRequest.of(0, 1000, Sort.by(Sort.Direction.ASC, "id"))
        );

        List<Long> resultIds = result.getContent().stream()
                .map(ClubJpaEntity::getId)
                .toList();
        assertThat(resultIds).contains(visibleClubId);
        assertThat(resultIds).doesNotContain(inactiveClubId, deletedClubId);
        assertThat(resultIds).isSorted();
    }

    @Test
    void shouldFindPublicClubByIdOnlyWhenActiveAndNonDeleted() {
        String suffix = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Long visibleClubId = insertClub("PBDV" + suffix, true, false);
        Long inactiveClubId = insertClub("PBDI" + suffix, false, false);
        Long deletedClubId = insertClub("PBDD" + suffix, true, true);

        Optional<ClubJpaEntity> visibleClub = clubJpaRepository.findByIdAndActiveTrueAndIsDeletedFalse(visibleClubId);
        Optional<ClubJpaEntity> inactiveClub = clubJpaRepository.findByIdAndActiveTrueAndIsDeletedFalse(inactiveClubId);
        Optional<ClubJpaEntity> deletedClub = clubJpaRepository.findByIdAndActiveTrueAndIsDeletedFalse(deletedClubId);

        assertThat(visibleClub).isPresent();
        assertThat(inactiveClub).isEmpty();
        assertThat(deletedClub).isEmpty();
    }

    private Long insertClub(String code, boolean active, boolean deleted) {
        jdbcTemplate.update("""
                        insert into clubs (
                            name,
                            short_name,
                            code,
                            league,
                            country,
                            competition_tier,
                            trend_direction,
                            beginner_accessibility,
                            is_active,
                            logo_url,
                            primary_color,
                            secondary_color,
                            created_at,
                            updated_at,
                            is_deleted
                        ) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?)
                        """,
                "Public Club " + code,
                code.substring(0, 6),
                code,
                "EPL",
                "England",
                "CHALLENGER",
                "STABLE",
                new BigDecimal("3.00"),
                active,
                null,
                "#111111",
                "#FFFFFF",
                deleted
        );
        return jdbcTemplate.queryForObject("select id from clubs where code = ?", Long.class, code);
    }
}
