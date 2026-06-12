package com.kopite.fd.club.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
class GetAllClubTagsServiceTest {

    @Mock
    private ClubTagRepository clubTagRepository;

    @InjectMocks
    private GetAllClubTagsService getAllClubTagsService;

    @Test
    void shouldGetAllClubTags() {
        LocalDateTime now = LocalDateTime.now();
        when(clubTagRepository.findAllActive()).thenReturn(List.of(
                new ClubTag(10L, 1L, "High pressing", "STYLE", 1, true, now, now),
                new ClubTag(20L, 2L, "European nights", "HISTORY", 1, true, now, now)
        ));

        List<ClubTagResult> results = getAllClubTagsService.getAllClubTags();

        assertThat(results).hasSize(2);
        assertThat(results).extracting(ClubTagResult::clubId).containsExactly(1L, 2L);
        verify(clubTagRepository).findAllActive();
    }
}
