package com.example.web.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.web.dto.*;
import com.example.web.entity.*;
import com.example.web.mapper.*;
import com.example.web.service.AiAnalyseService;
import com.example.web.tools.DeepSeekApiClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AiAnalyseServiceImpl implements AiAnalyseService {

    @Autowired
    private DeepSeekApiClient deepSeekApiClient;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private DietRecordMapper dietRecordMapper;

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private FoodUnitMapper foodUnitMapper;

    @Override
    public AiHealthAnalysisResponseDto analyzeUserHealth(Integer userId, Integer days) {
        try {
            AiHealthAnalysisRequestDto requestDto = buildUserHealthData(userId, days);
            return analyzeHealthData(requestDto);
        } catch (Exception e) {
            log.error("分析失败 userId={}", userId, e);
            return buildErrorResponse("数据获取失败：" + e.getMessage());
        }
    }

    @Override
    public AiHealthAnalysisResponseDto analyzeHealthData(AiHealthAnalysisRequestDto requestDto) {
        try {

            int score = calculateScore(requestDto);

            String prompt = buildAnalysisPrompt(requestDto, score);

            // ===================== ⭐关键修复 =====================
            AiHealthAnalysisResponseDto aiResponse =
                    deepSeekApiClient.analyzeHealth(prompt);

            if (aiResponse == null) {
                return buildErrorResponse("AI返回为空");
            }

            // ===================== 构造统一返回 =====================
            AiHealthAnalysisResponseDto response = new AiHealthAnalysisResponseDto();
            response.setSuccess(true);
            response.setAnalysisTime(LocalDateTime.now());

            AiHealthAnalysisResponseDto.AnalysisResult result =
                    aiResponse.getAnalysisResult();

            // ❗安全兜底（防止AI字段缺失）
            if (result == null) {
                result = new AiHealthAnalysisResponseDto.AnalysisResult();
                result.setScore(score);
                result.setEvaluation("一般");
                result.setProblems(new ArrayList<>());
                result.setSuggestions(new ArrayList<>());
            } else {

                if (result.getScore() == null) {
                    result.setScore(score);
                }

                if (result.getProblems() == null) {
                    result.setProblems(new ArrayList<>());
                }

                if (result.getSuggestions() == null) {
                    result.setSuggestions(new ArrayList<>());
                }
            }

            response.setAnalysisResult(result);

            return response;

        } catch (Exception e) {
            log.error("AI分析异常", e);
            return buildErrorResponse("AI分析失败：" + e.getMessage());
        }
    }

    // ===================== 评分 =====================
    private int calculateScore(AiHealthAnalysisRequestDto dto) {

        double calories = 0;
        double protein = 0;
        double fat = 0;

        if (dto.getDietRecords() != null) {
            for (AiHealthAnalysisRequestDto.DietData d : dto.getDietRecords()) {
                calories += safe(d.getCalories());
                protein += safe(d.getProtein());
                fat += safe(d.getFat());
            }
        }

        int score = 100;

        if (calories > 2400) score -= 15;
        if (calories < 1500) score -= 10;
        if (fat > 90) score -= 15;
        if (protein < 45) score -= 10;

        return Math.max(score, 0);
    }

    private double safe(Number v) {
        return v == null ? 0 : v.doubleValue();
    }

    // ===================== Prompt =====================
    private String buildAnalysisPrompt(AiHealthAnalysisRequestDto dto, int score) {

        double cal = 0, p = 0, f = 0, c = 0;

        if (dto.getDietRecords() != null) {
            for (AiHealthAnalysisRequestDto.DietData d : dto.getDietRecords()) {
                cal += safe(d.getCalories());
                p += safe(d.getProtein());
                f += safe(d.getFat());
                c += safe(d.getCarbohydrates());
            }
        }

        return """
你是专业健康分析AI，只输出JSON，不允许任何解释。

必须返回严格JSON格式：

{
  "score": %d,
  "evaluation": "良好|一般|较差",
  "problems": ["问题1"],
  "suggestions": ["建议1","建议2"]
}

数据：
热量:%f
蛋白质:%f
脂肪:%f
碳水:%f
""".formatted(score, cal, p, f, c);
    }

    // ===================== 用户数据 =====================
    private AiHealthAnalysisRequestDto buildUserHealthData(Integer userId, Integer days) {

        if (days == null || days <= 0) days = 7;

        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(days);

        AiHealthAnalysisRequestDto dto = new AiHealthAnalysisRequestDto();
        dto.setUserId(userId);
        dto.setStartTime(start);
        dto.setEndTime(end);

        AppUser user = appUserMapper.selectById(userId);

        if (user != null) {
            AiHealthAnalysisRequestDto.UserBasicInfo info =
                    new AiHealthAnalysisRequestDto.UserBasicInfo();

            info.setName(user.getName());

            if (user.getBirth() != null) {
                info.setAge(
                        Period.between(user.getBirth().toLocalDate(), LocalDate.now()).getYears()
                );
            }

            dto.setUserBasicInfo(info);
        }

        List<DietRecord> list = dietRecordMapper.selectList(
                Wrappers.<DietRecord>lambdaQuery()
                        .eq(DietRecord::getRecordUserId, userId)
                        .between(DietRecord::getRecordTime, start, end)
        );

        if (list != null) {
            dto.setDietRecords(list.stream().map(r -> {

                AiHealthAnalysisRequestDto.DietData d =
                        new AiHealthAnalysisRequestDto.DietData();

                Food f = foodMapper.selectById(r.getFoodId());
                FoodUnit u = foodUnitMapper.selectById(r.getFoodUnitId());

                if (f != null && u != null) {
                    double factor = safe(u.getUnitValue()) * safe(r.getRecordValue());

                    d.setFoodName(f.getName());
                    d.setCalories(f.getCalories() * factor);
                    d.setProtein(f.getProtein() * factor);
                    d.setFat(f.getFat() * factor);
                    d.setCarbohydrates(f.getCarbohydrates() * factor);
                }

                return d;
            }).collect(Collectors.toList()));
        }

        return dto;
    }

    // ===================== error =====================
    private AiHealthAnalysisResponseDto buildErrorResponse(String msg) {
        AiHealthAnalysisResponseDto r = new AiHealthAnalysisResponseDto();
        r.setSuccess(false);
        r.setErrorMessage(msg);
        r.setAnalysisTime(LocalDateTime.now());
        return r;
    }
}
