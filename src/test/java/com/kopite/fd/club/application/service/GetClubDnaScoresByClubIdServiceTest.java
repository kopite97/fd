package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.club.application.query.GetClubDnaScoresByClubIdQuery;
import com.kopite.fd.club.application.result.ClubDnaScoreResult;
import com.kopite.fd.club.domain.model.ClubDnaScore;
import com.kopite.fd.club.domain.repository.ClubDnaScoreRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetClubDnaScoresByClubIdServiceTest {

    @Mock
    private ClubDnaScoreRepository clubDnaScoreRepository;

    @InjectMocks
    private GetClubDnaScoresByClubIdService getClubDnaScoresByClubIdService;

    @Test
    void shouldGetClubDnaScoresByClubIdAndDataVersion() {
        LocalDateTime now = LocalDateTime.now();
        when(clubDnaScoreRepository.findByClubIdAndDataVersion(1L, "club-v1")).thenReturn(List.of(new ClubDnaScore(
                10L,
                1L,
                100L,
                new BigDecimal("4.50"),
                true,
                "club-v1",
                "EMOTIONAL",
                "fan_culture",
                "Fan Culture",
                1,
                now,
                now
        )));

        List<ClubDnaScoreResult> results = getClubDnaScoresByClubIdService.getClubDnaScoresByClubId(
                new GetClubDnaScoresByClubIdQuery(1L, " club-v1 ")
        );

        assertThat(results).hasSize(1);
        assertThat(results.get(0).dnaKey()).isEqualTo("fan_culture");
        assertThat(results.get(0).score()).isEqualByComparingTo("4.50");
        verify(clubDnaScoreRepository).findByClubIdAndDataVersion(1L, "club-v1");
    }

    @Test
    void shouldRejectNullClubId() {
        assertThatThrownBy(() -> getClubDnaScoresByClubIdService.getClubDnaScoresByClubId(
                new GetClubDnaScoresByClubIdQuery(null, "club-v1")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("clubId must not be null.");
    }

    @Test
    void shouldRejectBlankDataVersion() {
        assertThatThrownBy(() -> getClubDnaScoresByClubIdService.getClubDnaScoresByClubId(
                new GetClubDnaScoresByClubIdQuery(1L, " ")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("dataVersion must not be blank.");
    }
}
