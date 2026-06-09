package com.kopite.fd.assessment.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionScoreMappingJpaRepository extends JpaRepository<OptionScoreMappingJpaEntity, Long> {

    List<OptionScoreMappingJpaEntity> findByOptionIdIn(List<Long> optionIds);
}
