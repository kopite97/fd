package com.kopite.fd.assessment.infrastructure.adapter;

import com.kopite.fd.assessment.domain.model.DnaDefinition;
import com.kopite.fd.assessment.domain.repository.DnaDefinitionRepository;
import com.kopite.fd.assessment.infrastructure.entity.DnaDefinitionJpaEntity;
import com.kopite.fd.assessment.infrastructure.repository.DnaDefinitionJpaRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DnaDefinitionPersistenceAdapter implements DnaDefinitionRepository {

    private final DnaDefinitionJpaRepository dnaDefinitionJpaRepository;

    @Override
    public List<DnaDefinition> findActiveDefinitions() {
        return dnaDefinitionJpaRepository.findByActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc().stream()
                .map(DnaDefinitionJpaEntity::toDomain)
                .toList();
    }
}
