package com.kopite.fd.assessment.controller;

import com.kopite.fd.assessment.application.CompleteAssessmentCommand;
import com.kopite.fd.assessment.application.CompleteAssessmentResult;
import com.kopite.fd.assessment.application.CompleteAssessmentService;
import com.kopite.fd.assessment.application.GetAssessmentQuestionsQuery;
import com.kopite.fd.assessment.application.GetAssessmentQuestionsResult;
import com.kopite.fd.assessment.application.GetAssessmentQuestionsService;
import com.kopite.fd.assessment.application.StartAssessmentCommand;
import com.kopite.fd.assessment.application.StartAssessmentResult;
import com.kopite.fd.assessment.application.StartAssessmentService;
import com.kopite.fd.assessment.application.SubmitAssessmentAnswerCommand;
import com.kopite.fd.assessment.application.SubmitAssessmentAnswerResult;
import com.kopite.fd.assessment.application.SubmitAssessmentAnswerService;
import com.kopite.fd.assessment.dto.request.CompleteAssessmentRequest;
import com.kopite.fd.assessment.dto.request.StartAssessmentRequest;
import com.kopite.fd.assessment.dto.request.SubmitAssessmentAnswerRequest;
import com.kopite.fd.assessment.dto.response.CompleteAssessmentResponse;
import com.kopite.fd.assessment.dto.response.GetAssessmentQuestionsResponse;
import com.kopite.fd.assessment.dto.response.StartAssessmentResponse;
import com.kopite.fd.assessment.dto.response.SubmitAssessmentAnswerResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assessments")
public class AssessmentController {

    private final StartAssessmentService startAssessmentService;
    private final GetAssessmentQuestionsService getAssessmentQuestionsService;
    private final SubmitAssessmentAnswerService submitAssessmentAnswerService;
    private final CompleteAssessmentService completeAssessmentService;

    public AssessmentController(
            StartAssessmentService startAssessmentService,
            GetAssessmentQuestionsService getAssessmentQuestionsService,
            SubmitAssessmentAnswerService submitAssessmentAnswerService,
            CompleteAssessmentService completeAssessmentService
    ) {
        this.startAssessmentService = startAssessmentService;
        this.getAssessmentQuestionsService = getAssessmentQuestionsService;
        this.submitAssessmentAnswerService = submitAssessmentAnswerService;
        this.completeAssessmentService = completeAssessmentService;
    }

    @PostMapping
    public StartAssessmentResponse startAssessment(@Valid @RequestBody StartAssessmentRequest request) {
        StartAssessmentResult result = startAssessmentService.start(new StartAssessmentCommand(
                request.userId(),
                request.anonymousId(),
                request.questionVersion()
        ));

        return StartAssessmentResponse.from(result);
    }

    @GetMapping("/{assessmentId}/questions")
    public GetAssessmentQuestionsResponse getAssessmentQuestions(@PathVariable Long assessmentId) {
        GetAssessmentQuestionsResult result = getAssessmentQuestionsService.getQuestions(
                new GetAssessmentQuestionsQuery(assessmentId)
        );

        return GetAssessmentQuestionsResponse.from(result);
    }

    @PostMapping("/{assessmentId}/answers")
    public SubmitAssessmentAnswerResponse submitAssessmentAnswer(
            @PathVariable Long assessmentId,
            @Valid @RequestBody SubmitAssessmentAnswerRequest request
    ) {
        SubmitAssessmentAnswerResult result = submitAssessmentAnswerService.submit(new SubmitAssessmentAnswerCommand(
                assessmentId,
                request.questionId(),
                request.optionId(),
                request.answerText()
        ));

        return SubmitAssessmentAnswerResponse.from(result);
    }

    @PostMapping("/{assessmentId}/complete")
    public CompleteAssessmentResponse completeAssessment(
            @PathVariable Long assessmentId,
            @Valid @RequestBody CompleteAssessmentRequest request
    ) {
        CompleteAssessmentResult result = completeAssessmentService.complete(new CompleteAssessmentCommand(
                assessmentId,
                request.algorithmVersion()
        ));

        return CompleteAssessmentResponse.from(result);
    }
}
