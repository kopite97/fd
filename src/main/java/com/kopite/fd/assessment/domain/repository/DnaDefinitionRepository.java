package com.kopite.fd.assessment.domain.repository;

import com.kopite.fd.assessment.domain.model.DnaDefinition;
import java.util.List;

public interface DnaDefinitionRepository {

    List<DnaDefinition> findActiveDefinitions();
}
