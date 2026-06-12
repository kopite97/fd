package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.kopite.fd.club.application.query.GetPublicClubByIdQuery;
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
class GetPublicClubByIdServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private GetPublicClubByIdService getPublicClubByIdService;

    @Test
    void shouldReturnPublicClubWhenVisible() {
        LocalDateTime now = LocalDateTime.now();
        when(clubRepository.findPublicClubById(1L)).thenReturn(Optional.of(new Club(
                1L,
                "Arsenal",
                "AFC",
                "ARS",
                "EPL",
                "England",
                "TITLE_CONTENDER",
                "RISING",
                new BigDecimal("4.50"),
                true,
                null,
                "#EF0107",
                "#FFFFFF",
                now,
                now
        )));

        Optional<ClubResult> result = getPublicClubByIdService.getPublicClubById(new GetPublicClubByIdQuery(1L));

        assertThat(result).isPresent();
        assertThat(result.get().code()).isEqualTo("ARS");
        assertThat(result.get().competitionTier()).isEqualTo("TITLE_CONTENDER");
    }

    @Test
    void shouldReturnEmptyWhenClubIsNotPubliclyVisible() {
        when(clubRepository.findPublicClubById(1L)).thenReturn(Optional.empty());

        Optional<ClubResult> result = getPublicClubByIdService.getPublicClubById(new GetPublicClubByIdQuery(1L));

        assertThat(result).isEmpty();
    }

    @Test
    void shouldRejectNullClubId() {
        assertThatThrownBy(() -> getPublicClubByIdService.getPublicClubById(new GetPublicClubByIdQuery(null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("clubId must not be null.");
    }
}
