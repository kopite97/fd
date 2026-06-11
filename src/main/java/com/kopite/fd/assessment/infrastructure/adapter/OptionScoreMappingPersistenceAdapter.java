package com.kopite.fd.assessment.infrastructure.adapter;

import com.kopite.fd.assessment.domain.model.OptionScoreMapping;
import com.kopite.fd.assessment.domain.repository.OptionScoreMappingRepository;
import com.kopite.fd.assessment.infrastructure.entity.OptionScoreMappingJpaEntity;
import com.kopite.fd.assessment.infrastructure.repository.OptionScoreMappingJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OptionScoreMappingPersistenceAdapter implements OptionScoreMappingRepository {

    private final OptionScoreMappingJpaRepository optionScoreMappingJpaRepository;

    @Override
    public List<OptionScoreMapping> findByOptionIds(List<Long> optionIds) {
        if (optionIds.isEmpty()) {
            return List.of();
        }

        return optionScoreMappingJpaRepository.findByOptionIdInAndIsDeletedFalse(optionIds).stream()
                .map(OptionScoreMappingJpaEntity::toDomain)
                .toList();
    }
}
