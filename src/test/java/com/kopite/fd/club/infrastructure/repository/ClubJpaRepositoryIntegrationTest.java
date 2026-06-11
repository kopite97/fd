package com.kopite.fd.club.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.infrastructure.entity.ClubJpaEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=update")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClubJpaRepositoryIntegrationTest {

    @Autowired
    private ClubJpaRepository clubJpaRepository;

    @Test
    void shouldPersistAndFindClubByCode() {
        String uniqueCode = "CLB" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Club club = new Club(
                null,
                "Club " + uniqueCode,
                "C" + uniqueCode.substring(uniqueCode.length() - 4),
                uniqueCode,
                "EPL",
                "England",
                "CHALLENGER",
                "STABLE",
                new BigDecimal("3.50"),
                true,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        ClubJpaEntity savedEntity = clubJpaRepository.save(ClubJpaEntity.fromDomain(club));
        Optional<ClubJpaEntity> foundEntity = clubJpaRepository.findByCodeAndIsDeletedFalse(uniqueCode);

        assertThat(savedEntity.getId()).isNotNull();
        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get().getCode()).isEqualTo(uniqueCode);
        assertThat(foundEntity.get().getShortName()).startsWith("C");
        assertThat(foundEntity.get().getBeginnerAccessibility()).isEqualByComparingTo("3.50");
    }
}
