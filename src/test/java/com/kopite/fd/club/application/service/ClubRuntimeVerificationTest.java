package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.kopite.fd.club.application.command.SaveClubCommand;
import com.kopite.fd.club.application.query.GetClubByCodeQuery;
import com.kopite.fd.club.application.result.ClubResult;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ClubRuntimeVerificationTest {

    @Autowired
    private SaveClubService saveClubService;

    @Autowired
    private GetClubByCodeService getClubByCodeService;

    @Test
    void shouldPersistAndRetrieveClubThroughRunningApplicationContext() {
        String uniqueCode = "RT" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        ClubResult savedClub = saveClubService.saveClub(new SaveClubCommand(
                null,
                "Runtime Club " + uniqueCode,
                "RC" + uniqueCode.substring(uniqueCode.length() - 4),
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

        Optional<ClubResult> retrievedClub = getClubByCodeService.getClubByCode(new GetClubByCodeQuery(uniqueCode));

        assertThat(savedClub.id()).isNotNull();
        assertThat(retrievedClub).isPresent();
        assertThat(retrievedClub.get().code()).isEqualTo(uniqueCode);
        assertThat(retrievedClub.get().name()).isEqualTo("Runtime Club " + uniqueCode);
        assertThat(retrievedClub.get().beginnerAccessibility()).isEqualByComparingTo("2.75");
    }
}
