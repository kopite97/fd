package com.kopite.fd.assessment.domain;

import java.util.List;

public interface DnaDefinitionRepository {

    List<DnaDefinition> findActiveDefinitions();
}
