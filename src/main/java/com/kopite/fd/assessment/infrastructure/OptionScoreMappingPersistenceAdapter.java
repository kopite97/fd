package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.OptionScoreMapping;
import com.kopite.fd.assessment.domain.OptionScoreMappingRepository;
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
