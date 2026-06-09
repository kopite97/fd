package com.kopite.fd.assessment.infrastructure.adapter;

import com.kopite.fd.assessment.domain.model.OptionScoreMapping;
import com.kopite.fd.assessment.domain.repository.OptionScoreMappingRepository;
import com.kopite.fd.assessment.infrastructure.entity.OptionScoreMappingJpaEntity;
import com.kopite.fd.assessment.infrastructure.repository.OptionScoreMappingJpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OptionScoreMappingPersistenceAdapter implements OptionScoreMappingRepository {

    private final OptionScoreMappingJpaRepository optionScoreMappingJpaRepository;

    public OptionScoreMappingPersistenceAdapter(OptionScoreMappingJpaRepository optionScoreMappingJpaRepository) {
        this.optionScoreMappingJpaRepository = optionScoreMappingJpaRepository;
    }

    @Override
    public List<OptionScoreMapping> findByOptionIds(List<Long> optionIds) {
        if (optionIds.isEmpty()) {
            return List.of();
        }

        return optionScoreMappingJpaRepository.findByOptionIdIn(optionIds).stream()
                .map(OptionScoreMappingJpaEntity::toDomain)
                .toList();
    }
}
