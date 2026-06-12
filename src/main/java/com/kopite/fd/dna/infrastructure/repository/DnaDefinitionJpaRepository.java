package com.kopite.fd.dna.infrastructure.repository;

import com.kopite.fd.dna.infrastructure.entity.DnaDefinitionJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DnaDefinitionJpaRepository extends JpaRepository<DnaDefinitionJpaEntity, Long> {

    List<DnaDefinitionJpaEntity> findByActiveTrueAndIsDeletedFalseOrderByDisplayOrderAsc();
}
