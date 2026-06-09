package com.kopite.fd.assessment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kopite.fd.assessment.application.result.CompleteAssessmentResult;
import com.kopite.fd.assessment.application.result.GetAssessmentQuestionsResult;
import com.kopite.fd.assessment.application.result.StartAssessmentResult;
import com.kopite.fd.assessment.application.result.SubmitAssessmentAnswerResult;
import com.kopite.fd.assessment.application.service.CompleteAssessmentService;
import com.kopite.fd.assessment.application.service.GetAssessmentQuestionsService;
import com.kopite.fd.assessment.application.service.StartAssessmentService;
import com.kopite.fd.assessment.application.service.SubmitAssessmentAnswerService;
import com.kopite.fd.assessment.domain.type.AssessmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AssessmentController.class)
class AssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StartAssessmentService startAssessmentService;

    @MockBean
    private GetAssessmentQuestionsService getAssessmentQuestionsService;

    @MockBean
    private SubmitAssessmentAnswerService submitAssessmentAnswerService;

    @MockBean
    private CompleteAssessmentService completeAssessmentService;

    @Test
    void shouldReturnBadRequestWhenQuestionVersionIsMissingOnStartAssessment() throws Exception {
        mockMvc.perform(post("/api/assessments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId":1}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDelegateStartAssessment() throws Exception {
        when(startAssessmentService.start(any())).thenReturn(new StartAssessmentResult(
                1L, 1L, null, AssessmentStatus.IN_PROGRESS, "v1",
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()
        ));

        mockMvc.perform(post("/api/assessments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"userId":1,"questionVersion":"v1"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assessmentId").value(1))
                .andExpect(jsonPath("$.questionVersion").value("v1"));
    }

    @Test
    void shouldDelegateGetQuestions() throws Exception {
        when(getAssessmentQuestionsService.getQuestions(any())).thenReturn(new GetAssessmentQuestionsResult(
                1L,
                "v1",
                List.of(new GetAssessmentQuestionsResult.AssessmentQuestionItem(
                        10L, "Q1", "CHOICE", 100L, 1,
                        List.of(new GetAssessmentQuestionsResult.AssessmentQuestionOptionItem(1000L, "A", 1))
                ))
        ));

        mockMvc.perform(get("/api/assessments/1/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assessmentId").value(1))
                .andExpect(jsonPath("$.questions[0].questionId").value(10));
    }

    @Test
    void shouldDelegateSubmitAnswer() throws Exception {
        when(submitAssessmentAnswerService.submit(any())).thenReturn(new SubmitAssessmentAnswerResult(
                11L, 1L, 10L, 1000L, null, LocalDateTime.now()
        ));

        mockMvc.perform(post("/api/assessments/1/answers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"questionId":10,"optionId":1000}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answerId").value(11));
    }

    @Test
    void shouldDelegateCompleteAssessment() throws Exception {
        when(completeAssessmentService.complete(any())).thenReturn(new CompleteAssessmentResult(
                1L,
                AssessmentStatus.COMPLETED,
                "alg-v1",
                LocalDateTime.now(),
                List.of(new CompleteAssessmentResult.FinalizedDnaScoreItem(200L, 8))
        ));

        mockMvc.perform(post("/api/assessments/1/complete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"algorithmVersion":"alg-v1"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.finalizedDnaScores[0].dnaDefinitionId").value(200));
    }
}
