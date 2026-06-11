package com.kopite.fd.club.infrastructure.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import com.kopite.fd.club.infrastructure.repository.ClubJpaRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClubPersistenceAdapterTest {

    @Mock
    private ClubJpaRepository clubJpaRepository;

    @InjectMocks
    private ClubPersistenceAdapter clubPersistenceAdapter;

    @Test
    void shouldDefaultInheritedSoftDeleteToFalseForNewClubPersistenceEntity() {
        Club club = new Club(
                1L,
                "Arsenal",
                "AFC",
                "ARS",
                "EPL",
                "England",
                "TITLE_CONTENDER",
                "RISING",
                BigDecimal.ZERO,
                true,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        assertThat(ClubJpaEntity.fromDomain(club).isDeleted()).isFalse();
    }

    @Test
    void shouldFindClubByCodeUsingNonDeletedScope() {
        ClubJpaEntity clubJpaEntity = org.mockito.Mockito.mock(ClubJpaEntity.class);
        Club club = new Club(
                1L,
                "Arsenal",
                "AFC",
                "ARS",
                "EPL",
                "England",
                "TITLE_CONTENDER",
                "RISING",
                BigDecimal.ZERO,
                true,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        when(clubJpaRepository.findByCodeAndIsDeletedFalse("ARS")).thenReturn(Optional.of(clubJpaEntity));
        when(clubJpaEntity.toDomain()).thenReturn(club);

        Optional<Club> result = clubPersistenceAdapter.findByCode("ARS");

        assertThat(result).contains(club);
        verify(clubJpaRepository).findByCodeAndIsDeletedFalse("ARS");
    }

    @Test
    void shouldRoundTripClubPersistenceEntity() {
        LocalDateTime now = LocalDateTime.now();
        Club club = new Club(
                1L,
                "Arsenal",
                "AFC",
                "ARS",
                "EPL",
                "England",
                "TITLE_CONTENDER",
                "RISING",
                new BigDecimal("4.25"),
                true,
                "https://example.com/logo.png",
                "#FF0000",
                "#FFFFFF",
                now,
                now
        );

        Club roundTrip = ClubJpaEntity.fromDomain(club).toDomain();

        assertThat(roundTrip.getCode()).isEqualTo("ARS");
        assertThat(roundTrip.getShortName()).isEqualTo("AFC");
        assertThat(roundTrip.getCompetitionTier()).isEqualTo("TITLE_CONTENDER");
        assertThat(roundTrip.getTrendDirection()).isEqualTo("RISING");
        assertThat(roundTrip.getBeginnerAccessibility()).isEqualByComparingTo("4.25");
    }
}
