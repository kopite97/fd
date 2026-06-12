package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.kopite.fd.club.application.command.SaveClubCommand;
import com.kopite.fd.club.application.query.GetClubTagsByClubIdQuery;
import com.kopite.fd.club.application.result.ClubResult;
import com.kopite.fd.club.application.result.ClubTagResult;
import com.kopite.fd.club.domain.model.ClubTag;
import com.kopite.fd.club.infrastructure.entity.ClubTagJpaEntity;
import com.kopite.fd.club.infrastructure.repository.ClubTagJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ClubTagRuntimeVerificationTest {

    @Autowired
    private SaveClubService saveClubService;

    @Autowired
    private GetClubTagsByClubIdService getClubTagsByClubIdService;

    @Autowired
    private GetAllClubTagsService getAllClubTagsService;

    @Autowired
    private ClubTagJpaRepository clubTagJpaRepository;

    @Test
    void shouldRetrieveClubTagsThroughRunningApplicationContext() {
        String uniqueCode = "TG" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        ClubResult savedClub = saveClubService.saveClub(new SaveClubCommand(
                null,
                "Runtime Tag Club " + uniqueCode,
                "TC" + uniqueCode.substring(uniqueCode.length() - 4),
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
        LocalDateTime now = LocalDateTime.now();
        clubTagJpaRepository.save(ClubTagJpaEntity.fromDomain(new ClubTag(
                null, savedClub.id(), "Runtime tag", "STYLE", 1, true, now, now
        )));

        List<ClubTagResult> clubTags = getClubTagsByClubIdService.getClubTagsByClubId(
                new GetClubTagsByClubIdQuery(savedClub.id())
        );
        List<ClubTagResult> allClubTags = getAllClubTagsService.getAllClubTags();

        assertThat(clubTags).hasSize(1);
        assertThat(clubTags.get(0).tagName()).isEqualTo("Runtime tag");
        assertThat(allClubTags).extracting(ClubTagResult::clubId).contains(savedClub.id());
    }
}
