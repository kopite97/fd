package com.kopite.fd.assessment.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DnaDefinitionJpaRepository extends JpaRepository<DnaDefinitionJpaEntity, Long> {

    List<DnaDefinitionJpaEntity> findByActiveTrueOrderByDisplayOrderAsc();
}
