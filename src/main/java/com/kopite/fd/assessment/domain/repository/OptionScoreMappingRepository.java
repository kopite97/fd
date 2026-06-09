package com.kopite.fd.assessment.domain.repository;

import com.kopite.fd.assessment.domain.model.OptionScoreMapping;
import java.util.List;

public interface OptionScoreMappingRepository {

    List<OptionScoreMapping> findByOptionIds(List<Long> optionIds);
}
