package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.club.application.query.GetClubTagsByClubIdQuery;
import com.kopite.fd.club.application.result.ClubTagResult;
import com.kopite.fd.club.domain.model.ClubTag;
import com.kopite.fd.club.domain.repository.ClubTagRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetClubTagsByClubIdServiceTest {

    @Mock
    private ClubTagRepository clubTagRepository;

    @InjectMocks
    private GetClubTagsByClubIdService getClubTagsByClubIdService;

    @Test
    void shouldGetClubTagsByClubId() {
        LocalDateTime now = LocalDateTime.now();
        when(clubTagRepository.findActiveByClubId(1L)).thenReturn(List.of(new ClubTag(
                10L,
                1L,
                "High pressing",
                "STYLE",
                1,
                true,
                now,
                now
        )));

        List<ClubTagResult> results = getClubTagsByClubIdService.getClubTagsByClubId(
                new GetClubTagsByClubIdQuery(1L)
        );

        assertThat(results).hasSize(1);
        assertThat(results.get(0).tagName()).isEqualTo("High pressing");
        assertThat(results.get(0).tagType()).isEqualTo("STYLE");
        verify(clubTagRepository).findActiveByClubId(1L);
    }

    @Test
    void shouldRejectNullClubId() {
        assertThatThrownBy(() -> getClubTagsByClubIdService.getClubTagsByClubId(
                new GetClubTagsByClubIdQuery(null)
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("clubId must not be null.");
    }
}
