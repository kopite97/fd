package com.kopite.fd.club.domain.repository;

import com.kopite.fd.club.domain.model.Club;
import com.kopite.fd.club.domain.repository.result.ClubPage;
import java.util.Optional;

public interface ClubRepository {

    Club save(Club club);

    Optional<Club> findById(Long clubId);

    Optional<Club> findByCode(String code);

    ClubPage findPublicClubs(int page, int size);

    Optional<Club> findPublicClubById(Long clubId);
}
