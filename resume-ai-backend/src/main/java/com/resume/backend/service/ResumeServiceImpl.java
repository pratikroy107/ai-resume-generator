package com.resume.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResumeServiceImpl implements ResumeService {

    private ChatClient chatClient;

    public ResumeServiceImpl(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // This service generates a resume response by combining a user's input with a predefined prompt template,
    // sending it to an AI model, and parsing the output into a structured format.
    @Override
    public Map<String, Object> generateResumeResponse(String userResumeDescription) throws IOException {

        // resume_prompt.txt is the predefined prompt template
        String promptString = this.loadPromptFromFile("resume_prompt.txt");

        //userDescription is the input of the user
        String promptContent = this.putValuesToTemplate(promptString, Map.of(
                "userDescription", userResumeDescription
        ));
        Prompt prompt = new Prompt(promptContent);
        String response = chatClient.prompt(prompt).call().content();
        Map<String, Object> stringObjectMap = parseMultipleResponses(response);
        //modify :
        return stringObjectMap;
    }

    String loadPromptFromFile(String filename) throws IOException {
        Path path = new ClassPathResource(filename).getFile().toPath();
        return Files.readString(path);
    }

    String putValuesToTemplate(String template, Map<String, String> values) {
        for (Map.Entry<String, String> entry : values.entrySet()) {
            template = template.replace("{{"+entry.getKey()+"}}", entry.getValue());
        }
        return template;
    }

    // function to parse AI response into structured data
    public static Map<String, Object> parseMultipleResponses(String response) {
        Map<String, Object> jsonResponse = new HashMap<>();

        // Extract content inside <think> tags
        int thinkStart = response.indexOf("<think>") + 7;
        int thinkEnd = response.indexOf("</think>");
        if (thinkStart != -1 && thinkEnd != -1) {
            String thinkContent = response.substring(thinkStart, thinkEnd).trim();
            jsonResponse.put("think", thinkContent);
        } else {
            jsonResponse.put("think", null); // Handle missing <think> tags
        }

        // Extract content that is in JSON format
        int jsonStart = response.indexOf("```json") + 7; // Start after ```json
        int jsonEnd = response.lastIndexOf("```");       // End before ```
        if (jsonStart != -1 && jsonEnd != -1 && jsonStart < jsonEnd) {
            String jsonContent = response.substring(jsonStart, jsonEnd).trim();
            try {
                // Convert JSON string to Map using Jackson ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> dataContent = objectMapper.readValue(jsonContent, Map.class);
                jsonResponse.put("data", dataContent);
            } catch (Exception e) {
                jsonResponse.put("data", null); // Handle invalid JSON
                System.err.println("Invalid JSON format in the response: " + e.getMessage());
            }
        } else {
            jsonResponse.put("data", null); // Handle missing JSON
        }

        return jsonResponse;
    }
}
