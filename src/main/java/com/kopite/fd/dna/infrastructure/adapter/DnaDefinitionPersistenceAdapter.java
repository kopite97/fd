package com.kopite.fd.dna.infrastructure.adapter;

import com.kopite.fd.dna.domain.model.DnaDefinition;
import com.kopite.fd.dna.domain.repository.DnaDefinitionRepository;
import com.kopite.fd.dna.infrastructure.entity.DnaDefinitionJpaEntity;
import com.kopite.fd.dna.infrastructure.repository.DnaDefinitionJpaRepository;
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
