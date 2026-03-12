package com.example.web.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.web.tools.dto.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct; // 注意：SpringBoot 3.x用jakarta，2.x用javax

/**
 * 全局响应处理切面
 */
@ControllerAdvice
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    // 注入Spring容器中已配置JSR310模块的ObjectMapper
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${server.port:7245}")
    private String serverPort;

    @Value("${server.ip:http://localhost:7245}")
    private String serverIp;

    // 补全：保留你原来的ObjectMapper自定义配置
    @PostConstruct
    public void initObjectMapperConfig() {
        // 原有配置：大小写不敏感 + 大驼峰命名策略
        objectMapper.configure(com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .setPropertyNamingStrategy(com.fasterxml.jackson.databind.PropertyNamingStrategy.UPPER_CAMEL_CASE);
    }

    public GlobalResponseAdvice() {
    }

    /**
     * 是否开启支持
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 对写入body之前进行拦截处理
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        Object result;
        if (body == null) {
            result = ResponseData.OfSuccess();
        } else if (body instanceof ResponseData<?>) {
            result = body;
        } else {
            result = ResponseData.GetResponseDataInstance(body, "成功", true);
        }

        // 处理响应内容中的URL替换
        if (result instanceof ResponseData<?>) {
            try {
                ResponseData<?> responseData = (ResponseData<?>) result;
                Object data = responseData.getData();
                if (data != null) {
                    // 使用注入的、已配置JSR310的ObjectMapper序列化
                    String jsonStr = objectMapper.writeValueAsString(result);

                    if (jsonStr.contains("http://localhost:" + serverPort + "/")) {
                        String newJsonStr = jsonStr.replace("http://localhost:" + serverPort + "/", serverIp + "/");
                        return objectMapper.readValue(newJsonStr, ResponseData.class);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

}