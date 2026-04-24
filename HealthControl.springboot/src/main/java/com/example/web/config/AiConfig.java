package com.example.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * AI配置（工程标准版）
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ai")
public class AiConfig {

    /**
     * DeepSeek API Key（建议放到 yml / 环境变量）
     */
    private String apiKey;

    /**
     * API地址
     */
    private String apiUrl = "https://api.deepseek.com/chat/completions";

    /**
     * 模型名称
     */
    private String model = "deepseek-chat";

    /**
     * 最大token
     */
    private Integer maxTokens = 4000;

    /**
     * 温度（控制随机性）
     */
    private Double temperature = 0.3;

    /**
     * 是否启用Mock模式
     */
    private Boolean mockMode = false;

    /**
     * 请求超时（毫秒）
     */
    private Integer timeout = 10000;
}
