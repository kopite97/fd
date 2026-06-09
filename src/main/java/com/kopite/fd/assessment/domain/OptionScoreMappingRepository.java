package com.kopite.fd.assessment.domain;

import java.util.List;

public interface OptionScoreMappingRepository {

    List<OptionScoreMapping> findByOptionIds(List<Long> optionIds);
}
