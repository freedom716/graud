package com.example.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * DeepSeek API请求DTO（工业级优化版）
 */
@Data
@Builder
public class DeepSeekRequestDto {

    /**
     * 模型名称
     */
    private String model;

    /**
     * 消息列表（支持多轮对话）
     */
    private List<Message> messages;

    /**
     * 响应格式（强制JSON输出）
     */
    @JsonProperty("response_format")
    private Map<String, String> responseFormat;

    /**
     * 最大token数
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 温度参数（0~1）
     */
    private Double temperature;

    @Data
    @Builder
    public static class Message {

        /**
         * system / user / assistant
         */
        private String role;

        /**
         * 消息内容
         */
        private String content;
    }
}
