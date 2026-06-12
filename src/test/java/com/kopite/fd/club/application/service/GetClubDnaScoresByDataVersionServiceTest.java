package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.club.application.query.GetClubDnaScoresByDataVersionQuery;
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
class GetClubDnaScoresByDataVersionServiceTest {

    @Mock
    private ClubDnaScoreRepository clubDnaScoreRepository;

    @InjectMocks
    private GetClubDnaScoresByDataVersionService getClubDnaScoresByDataVersionService;

    @Test
    void shouldGetClubDnaScoresByDataVersion() {
        LocalDateTime now = LocalDateTime.now();
        when(clubDnaScoreRepository.findByDataVersion("club-v1")).thenReturn(List.of(new ClubDnaScore(
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

        List<ClubDnaScoreResult> results = getClubDnaScoresByDataVersionService.getClubDnaScoresByDataVersion(
                new GetClubDnaScoresByDataVersionQuery(" club-v1 ")
        );

        assertThat(results).hasSize(1);
        assertThat(results.get(0).dataVersion()).isEqualTo("club-v1");
        verify(clubDnaScoreRepository).findByDataVersion("club-v1");
    }

    @Test
    void shouldRejectBlankDataVersion() {
        assertThatThrownBy(() -> getClubDnaScoresByDataVersionService.getClubDnaScoresByDataVersion(
                new GetClubDnaScoresByDataVersionQuery(" ")
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("dataVersion must not be blank.");
    }
}
