package com.kopite.fd.club.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ClubDnaScoreTest {

    @Test
    void shouldCreateClubDnaScoreWhenScoreIsWithinMvpRange() {
        LocalDateTime now = LocalDateTime.now();

        ClubDnaScore clubDnaScore = new ClubDnaScore(
                1L,
                10L,
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
        );

        assertThat(clubDnaScore.getScore()).isEqualByComparingTo("4.50");
        assertThat(clubDnaScore.getDataVersion()).isEqualTo("club-v1");
    }

    @Test
    void shouldRejectScoreBelowMvpRange() {
        assertThatThrownBy(() -> new ClubDnaScore(
                1L,
                10L,
                100L,
                new BigDecimal("0.99"),
                false,
                "club-v1",
                null,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("score must be between 1.00 and 5.00.");
    }

    @Test
    void shouldRejectScoreAboveMvpRange() {
        assertThatThrownBy(() -> new ClubDnaScore(
                1L,
                10L,
                100L,
                new BigDecimal("5.01"),
                false,
                "club-v1",
                null,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("score must be between 1.00 and 5.00.");
    }

    @Test
    void shouldRejectBlankDataVersion() {
        assertThatThrownBy(() -> new ClubDnaScore(
                1L,
                10L,
                100L,
                new BigDecimal("3.00"),
                false,
                " ",
                null,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("dataVersion must not be blank.");
    }
}
