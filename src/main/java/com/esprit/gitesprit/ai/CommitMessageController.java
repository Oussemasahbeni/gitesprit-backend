package com.esprit.gitesprit.ai;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommitMessageController {

    private static final Logger log = LoggerFactory.getLogger(CommitMessageController.class);
    private final CommitMessageService commitMessageService;


    @PostMapping("/generate-commit-message")
    public ResponseEntity<GenerateCommitResponse> generateCommitMessage(
            @RequestBody GenerateCommitRequest request) {

        log.info("Received request to generate commit message.");
        if (request == null || request.diff() == null) {
            log.warn("Received invalid request body (null or missing diff).");
            return ResponseEntity.badRequest().body(new GenerateCommitResponse("Error: Request body or diff content is missing."));
        }

        try {
            String generatedMessage = commitMessageService.generateCommitMessage(request.diff());
            GenerateCommitResponse response = new GenerateCommitResponse(generatedMessage);
            log.info("Successfully generated commit message.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("An unexpected error occurred during commit message generation: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(new GenerateCommitResponse("Error: Failed to generate commit message due to an internal server error."));
        }
    }
}
