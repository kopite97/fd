package com.kopite.fd.club.domain.repository;

import com.kopite.fd.club.domain.model.ClubTag;
import java.util.List;

public interface ClubTagRepository {

    List<ClubTag> findActiveByClubId(Long clubId);

    List<ClubTag> findAllActive();
}
