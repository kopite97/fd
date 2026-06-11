package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kopite.fd.club.application.command.SaveClubCommand;
import com.kopite.fd.club.application.result.ClubResult;
import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.domain.repository.ClubRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SaveClubServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private SaveClubService saveClubService;

    @Test
    void shouldSaveClubWithTrimmedAndDefaultedValues() {
        LocalDateTime now = LocalDateTime.now();
        when(clubRepository.findByCode("ARS")).thenReturn(Optional.empty());
        when(clubRepository.save(any(Club.class))).thenAnswer(invocation -> {
            Club club = invocation.getArgument(0);
            return new Club(
                    1L,
                    club.getName(),
                    club.getShortName(),
                    club.getCode(),
                    club.getLeague(),
                    club.getCountry(),
                    club.getCompetitionTier(),
                    club.getTrendDirection(),
                    club.getBeginnerAccessibility(),
                    club.isActive(),
                    club.getLogoUrl(),
                    club.getPrimaryColor(),
                    club.getSecondaryColor(),
                    now,
                    now
            );
        });

        ClubResult result = saveClubService.saveClub(new SaveClubCommand(
                null,
                " Arsenal ",
                " AFC ",
                " ARS ",
                " EPL ",
                " England ",
                " TITLE_CONTENDER ",
                " RISING ",
                null,
                null,
                " https://example.com/logo.png ",
                " #FF0000 ",
                " #FFFFFF "
        ));

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Arsenal");
        assertThat(result.shortName()).isEqualTo("AFC");
        assertThat(result.code()).isEqualTo("ARS");
        assertThat(result.beginnerAccessibility()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.active()).isTrue();
        assertThat(result.logoUrl()).isEqualTo("https://example.com/logo.png");
        assertThat(result.primaryColor()).isEqualTo("#FF0000");
        assertThat(result.secondaryColor()).isEqualTo("#FFFFFF");
    }

    @Test
    void shouldRejectBlankRequiredFields() {
        SaveClubCommand command = new SaveClubCommand(
                null,
                " ",
                "AFC",
                "ARS",
                "EPL",
                "England",
                "TITLE_CONTENDER",
                "RISING",
                BigDecimal.ONE,
                true,
                null,
                null,
                null
        );

        assertThatThrownBy(() -> saveClubService.saveClub(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("name must not be blank.");
    }
}
