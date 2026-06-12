package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.kopite.fd.club.application.query.GetPublicClubsQuery;
import com.kopite.fd.club.application.result.ClubPageResult;
import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.domain.repository.ClubRepository;
import com.kopite.fd.club.domain.repository.result.ClubPage;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetPublicClubsServiceTest {

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private GetPublicClubsService getPublicClubsService;

    @Test
    void shouldReturnPublicClubsPage() {
        LocalDateTime now = LocalDateTime.now();
        when(clubRepository.findPublicClubs(0, 20)).thenReturn(new ClubPage(List.of(new Club(
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
        )), 0, 20, 1L, 1));

        ClubPageResult result = getPublicClubsService.getPublicClubs(new GetPublicClubsQuery(0, 20));

        assertThat(result.clubs()).hasSize(1);
        assertThat(result.clubs().get(0).code()).isEqualTo("ARS");
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(20);
        assertThat(result.totalElements()).isEqualTo(1L);
        assertThat(result.totalPages()).isEqualTo(1);
    }

    @Test
    void shouldRejectNegativePage() {
        assertThatThrownBy(() -> getPublicClubsService.getPublicClubs(new GetPublicClubsQuery(-1, 20)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("page must not be negative.");
    }

    @Test
    void shouldRejectNonPositiveSize() {
        assertThatThrownBy(() -> getPublicClubsService.getPublicClubs(new GetPublicClubsQuery(0, 0)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("size must be positive.");
    }
}
