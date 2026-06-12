package com.kopite.fd.club.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.domain.model.ClubTag;
import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import com.kopite.fd.club.infrastructure.entity.ClubTagJpaEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=update")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClubTagJpaRepositoryIntegrationTest {

    @Autowired
    private ClubJpaRepository clubJpaRepository;

    @Autowired
    private ClubTagJpaRepository clubTagJpaRepository;

    @Test
    void shouldFindActiveNonDeletedClubTagsByClubIdInDeterministicOrder() {
        Long clubId = saveClub("TAGA");
        LocalDateTime now = LocalDateTime.now();

        ClubTagJpaEntity second = clubTagJpaRepository.save(ClubTagJpaEntity.fromDomain(new ClubTag(
                null, clubId, "Second", "STYLE", 2, true, now, now
        )));
        ClubTagJpaEntity first = clubTagJpaRepository.save(ClubTagJpaEntity.fromDomain(new ClubTag(
                null, clubId, "First", "CULTURE", 1, true, now, now
        )));
        clubTagJpaRepository.save(ClubTagJpaEntity.fromDomain(new ClubTag(
                null, clubId, "Inactive", "STYLE", 0, false, now, now
        )));
        ClubTagJpaEntity deleted = clubTagJpaRepository.save(ClubTagJpaEntity.fromDomain(new ClubTag(
                null, clubId, "Deleted", "STYLE", 0, true, now, now
        )));
        ReflectionTestUtils.setField(deleted, "isDeleted", true);
        clubTagJpaRepository.save(deleted);

        List<ClubTagJpaEntity> results = clubTagJpaRepository
                .findByClubIdAndActiveTrueAndIsDeletedFalseOrderByDisplayOrderAscIdAsc(clubId);

        assertThat(results).extracting(ClubTagJpaEntity::getId)
                .containsExactly(first.getId(), second.getId());
    }

    @Test
    void shouldFindAllActiveNonDeletedClubTagsInDeterministicOrder() {
        Long firstClubId = saveClub("TAGB");
        Long secondClubId = saveClub("TAGC");
        LocalDateTime now = LocalDateTime.now();

        ClubTagJpaEntity secondClubTag = clubTagJpaRepository.save(ClubTagJpaEntity.fromDomain(new ClubTag(
                null, secondClubId, "Second club", "STYLE", 1, true, now, now
        )));
        ClubTagJpaEntity firstClubTag = clubTagJpaRepository.save(ClubTagJpaEntity.fromDomain(new ClubTag(
                null, firstClubId, "First club", "CULTURE", 1, true, now, now
        )));

        List<ClubTagJpaEntity> results = clubTagJpaRepository
                .findByActiveTrueAndIsDeletedFalseOrderByClubIdAscDisplayOrderAscIdAsc();

        assertThat(results).extracting(ClubTagJpaEntity::getId)
                .containsSubsequence(firstClubTag.getId(), secondClubTag.getId());
    }

    private Long saveClub(String prefix) {
        String uniqueCode = prefix + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Club club = new Club(
                null,
                "Club " + uniqueCode,
                "C" + uniqueCode.substring(uniqueCode.length() - 4),
                uniqueCode,
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
}
