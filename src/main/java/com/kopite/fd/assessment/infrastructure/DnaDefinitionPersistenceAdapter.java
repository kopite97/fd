package com.kopite.fd.assessment.infrastructure;

import com.kopite.fd.assessment.domain.DnaDefinition;
import com.kopite.fd.assessment.domain.DnaDefinitionRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DnaDefinitionPersistenceAdapter implements DnaDefinitionRepository {

    private final DnaDefinitionJpaRepository dnaDefinitionJpaRepository;

    public DnaDefinitionPersistenceAdapter(DnaDefinitionJpaRepository dnaDefinitionJpaRepository) {
        this.dnaDefinitionJpaRepository = dnaDefinitionJpaRepository;
    }

    @Override
    public List<DnaDefinition> findActiveDefinitions() {
        return dnaDefinitionJpaRepository.findByActiveTrueOrderByDisplayOrderAsc().stream()
                .map(DnaDefinitionJpaEntity::toDomain)
                .toList();
    }
}
