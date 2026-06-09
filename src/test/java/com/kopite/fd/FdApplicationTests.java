package com.kopite.fd;

import com.kopite.fd.assessment.domain.repository.AssessmentAnswerRepository;
import com.kopite.fd.assessment.domain.repository.AssessmentDnaScoreRepository;
import com.kopite.fd.assessment.domain.repository.AssessmentQuestionOptionRepository;
import com.kopite.fd.assessment.domain.repository.AssessmentQuestionRepository;
import com.kopite.fd.assessment.domain.repository.AssessmentRepository;
import com.kopite.fd.assessment.domain.repository.DnaDefinitionRepository;
import com.kopite.fd.assessment.domain.repository.OptionScoreMappingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=" +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
class FdApplicationTests {

    @MockitoBean
    private AssessmentRepository assessmentRepository;

    @MockitoBean
    private AssessmentAnswerRepository assessmentAnswerRepository;

    @MockitoBean
    private AssessmentDnaScoreRepository assessmentDnaScoreRepository;

    @MockitoBean
    private AssessmentQuestionRepository assessmentQuestionRepository;

    @MockitoBean
    private AssessmentQuestionOptionRepository assessmentQuestionOptionRepository;

    @MockitoBean
    private DnaDefinitionRepository dnaDefinitionRepository;

    @MockitoBean
    private OptionScoreMappingRepository optionScoreMappingRepository;

    @Test
    void contextLoads() {
    }

}
