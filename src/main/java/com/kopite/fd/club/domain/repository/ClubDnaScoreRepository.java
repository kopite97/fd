package com.kopite.fd.club.domain.repository;

import com.kopite.fd.club.domain.model.ClubDnaScore;
import java.util.List;

public interface ClubDnaScoreRepository {

    List<ClubDnaScore> findByClubIdAndDataVersion(Long clubId, String dataVersion);

    List<ClubDnaScore> findByDataVersion(String dataVersion);
}
