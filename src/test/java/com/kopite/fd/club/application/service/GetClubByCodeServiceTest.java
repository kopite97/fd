package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.kopite.fd.club.application.query.GetClubByCodeQuery;
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
class GetClubByCodeServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private GetClubByCodeService getClubByCodeService;

    @Test
    void shouldReturnClubWhenCodeExists() {
        LocalDateTime now = LocalDateTime.now();
        when(clubRepository.findByCode("ARS")).thenReturn(Optional.of(new Club(
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
                null,
                null,
                now,
                now
        )));

        Optional<ClubResult> result = getClubByCodeService.getClubByCode(new GetClubByCodeQuery(" ARS "));

        assertThat(result).isPresent();
        assertThat(result.get().code()).isEqualTo("ARS");
        assertThat(result.get().name()).isEqualTo("Arsenal");
    }

    @Test
    void shouldRejectBlankCode() {
        assertThatThrownBy(() -> getClubByCodeService.getClubByCode(new GetClubByCodeQuery(" ")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("code must not be blank.");
    }
}
