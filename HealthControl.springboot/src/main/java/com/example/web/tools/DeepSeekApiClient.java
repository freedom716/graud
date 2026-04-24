package com.example.web.tools;

import com.example.web.config.AiConfig;
import com.example.web.dto.DeepSeekRequestDto;
import com.example.web.dto.AiHealthAnalysisResponseDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Component
public class DeepSeekApiClient {

    @Autowired
    private AiConfig aiConfig;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * ⭐ 最终正确返回结构
     */
    public AiHealthAnalysisResponseDto analyzeHealth(String prompt) {

        AiHealthAnalysisResponseDto responseDto = new AiHealthAnalysisResponseDto();

        try {

            DeepSeekRequestDto request = buildRequest(prompt);

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + aiConfig.getApiKey());
            headers.put("Content-Type", "application/json");

            String responseStr = Boolean.TRUE.equals(aiConfig.getMockMode())
                    ? readMockResponse()
                    : HttpUtils.Post(aiConfig.getApiUrl(), request, headers);

            if (responseStr == null || responseStr.isBlank()) {
                responseDto.setSuccess(false);
                responseDto.setErrorMessage("AI返回为空");
                return responseDto;
            }

            String content = extractContent(responseStr);
            content = cleanContent(content);

            if (content == null || content.isBlank()) {
                responseDto.setSuccess(false);
                responseDto.setErrorMessage("AI content为空");
                return responseDto;
            }

            log.info("AI最终内容：{}", content);

            // ⭐ 关键：解析内部类
            AiHealthAnalysisResponseDto.AnalysisResult result =
                    objectMapper.readValue(
                            content,
                            AiHealthAnalysisResponseDto.AnalysisResult.class
                    );

            responseDto.setSuccess(true);
            responseDto.setAnalysisResult(result);
            responseDto.setAnalysisTime(LocalDateTime.now());

            return responseDto;

        } catch (Exception e) {
            log.error("AI分析失败", e);
            responseDto.setSuccess(false);
            responseDto.setErrorMessage(e.getMessage());
            return responseDto;
        }
    }

    // ===================== 请求构建 =====================
    private DeepSeekRequestDto buildRequest(String prompt) {

        Map<String, String> format = new HashMap<>();
        format.put("type", "json_object");

        List<DeepSeekRequestDto.Message> messages = new ArrayList<>();
        messages.add(buildMessage("system", getSystemPrompt()));
        messages.add(buildMessage("user", prompt));

        return DeepSeekRequestDto.builder()
                .model(aiConfig.getModel())
                .messages(messages)
                .responseFormat(format)
                .maxTokens(aiConfig.getMaxTokens())
                .temperature(aiConfig.getTemperature())
                .build();
    }

    private DeepSeekRequestDto.Message buildMessage(String role, String content) {
        return DeepSeekRequestDto.Message.builder()
                .role(role)
                .content(content)
                .build();
    }

    // ===================== 提取 content =====================
    private String extractContent(String responseStr) {
        try {
            var root = objectMapper.readTree(responseStr);
            return root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText(null);
        } catch (Exception e) {
            return null;
        }
    }

    // ===================== 清洗 =====================
    private String cleanContent(String content) {
        return content
                .replace("```json", "")
                .replace("```", "")
                .trim();
    }

    // ===================== mock =====================
    private String readMockResponse() {
        try {
            return Files.readString(Paths.get("external-resources/airesult.txt"));
        } catch (IOException e) {
            return """
            {
              "choices": [
                {
                  "message": {
                    "content": "{\\"score\\":80,\\"evaluation\\":\\"良好\\",\\"summary\\":\\"正常\\",\\"problems\\":[],\\"suggestions\\":[]}"
                  }
                }
              ]
            }
            """;
        }
    }

    private String getSystemPrompt() {
        return """
你是专业健康分析AI，只输出JSON，不允许解释。
必须包含：
score, evaluation, summary, risks, nutrition, sport, problems, suggestions
""";
    }
}
