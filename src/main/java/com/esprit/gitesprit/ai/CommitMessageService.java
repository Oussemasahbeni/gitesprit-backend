package com.esprit.gitesprit.ai;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CommitMessageService {

    private static final Logger log = LoggerFactory.getLogger(CommitMessageService.class);

    private final ChatClient chatClient;

    private static final String PROMPT_TEMPLATE = """
            Generate a concise Git commit message written in the present tense, following the Conventional Commits specification (e.g., 'feat: add login feature', 'fix: resolve null pointer in user service', 'chore: update dependencies').
            The commit message should accurately summarize the following code changes provided in the git diff format:

            --- GIT DIFF ---
            {diff}
            --- END GIT DIFF ---

            Analyze the diff and provide only the commit message, without any additional explanation or introductory text.
            """;

    public CommitMessageService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }


    public String generateCommitMessage(String diff) {
        if (diff == null || diff.isBlank()) {
            log.warn("Received empty or null diff.");
            // Return a default message or throw an exception based on requirements
            return "chore: empty changes detected";
        }

        log.info("Generating commit message for diff starting with: {}", diff.substring(0, Math.min(diff.length(), 100)));

        try {
            PromptTemplate promptTemplate = new PromptTemplate(PROMPT_TEMPLATE);
            Prompt prompt = promptTemplate.create(Map.of("diff", diff));
            return  chatClient.prompt(prompt).call().content().trim().replace("\"", "");


        } catch (Exception e) {
            log.error("Error calling AI service to generate commit message: {}", e.getMessage(), e);
            return "Error generating commit message. Please write one manually.";
        }
    }
}
