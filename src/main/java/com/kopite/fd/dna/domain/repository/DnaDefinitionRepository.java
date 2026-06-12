package com.kopite.fd.dna.domain.repository;

import com.kopite.fd.dna.domain.model.DnaDefinition;
import java.util.List;

public interface DnaDefinitionRepository {

    List<DnaDefinition> findActiveDefinitions();
}
