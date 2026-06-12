package com.kopite.fd.club.application.service;

import com.kopite.fd.club.application.result.ClubTagResult;
import com.kopite.fd.club.domain.model.ClubTag;
import com.kopite.fd.club.domain.repository.ClubTagRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetAllClubTagsService {

    private final ClubTagRepository clubTagRepository;

    public List<ClubTagResult> getAllClubTags() {
        return clubTagRepository.findAllActive().stream()
                .map(this::toResult)
                .toList();
    }

    private ClubTagResult toResult(ClubTag clubTag) {
        return new ClubTagResult(
                clubTag.getId(),
                clubTag.getClubId(),
                clubTag.getTagName(),
                clubTag.getTagType(),
                clubTag.getDisplayOrder(),
                clubTag.isActive(),
                clubTag.getCreatedAt(),
                clubTag.getUpdatedAt()
        );
    }
}
