package com.example.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.web.dto.VoiceFoodItemDto;
import com.example.web.dto.VoiceRecognizeRequestDto;
import com.example.web.dto.VoiceRecognizeResponseDto;
import com.example.web.entity.DietRecord;
import com.example.web.entity.Food;
import com.example.web.entity.FoodUnit;
import com.example.web.mapper.DietRecordMapper;
import com.example.web.mapper.FoodMapper;
import com.example.web.mapper.FoodUnitMapper;
import com.example.web.service.VoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VoiceServiceImpl implements VoiceService {

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private FoodUnitMapper foodUnitMapper;

    @Autowired
    private DietRecordMapper dietRecordMapper;

    private static final Pattern ARABIC_COUNT_PATTERN = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(个 | 碗 | 杯 | 份 | 克|g|ml|毫升 | 片 | 块 | 根 | 只)?", Pattern.CASE_INSENSITIVE);

    @Override
    public VoiceRecognizeResponseDto recognizeAndSave(Integer userId, MultipartFile file) {
        VoiceRecognizeRequestDto requestDto = new VoiceRecognizeRequestDto();
        requestDto.setUserId(userId);
        requestDto.setText("");
        return recognizeTextAndSave(requestDto);
    }

    @Override
    public VoiceRecognizeResponseDto recognizeTextAndSave(VoiceRecognizeRequestDto requestDto) {
        VoiceRecognizeResponseDto responseDto = new VoiceRecognizeResponseDto();

        String text = requestDto.getText() == null ? "" : requestDto.getText().trim();
        responseDto.setText(text);
        responseDto.setSavedCount(0);

        if (requestDto.getUserId() == null || requestDto.getUserId() <= 0 || text.isEmpty()) {
            return responseDto;
        }

        List<Food> foods = foodMapper.selectList(Wrappers.<Food>lambdaQuery().orderByAsc(Food::getId));
        if (foods == null || foods.isEmpty()) {
            responseDto.getUnmatchedTexts().add(text);
            return responseDto;
        }

        List<VoiceFragment> fragments = parseVoiceText(text);
        int saved = 0;

        for (VoiceFragment fragment : fragments) {
            Food matchFood = findBestFoodMatch(fragment.normalizedText, foods);
            if (matchFood == null) {
                responseDto.getUnmatchedTexts().add(fragment.sourceText);
                continue;
            }

            FoodUnit unit = chooseFoodUnit(matchFood.getId(), fragment.unitKeyword);
            if (unit == null) {
                responseDto.getUnmatchedTexts().add(fragment.sourceText);
                continue;
            }

            DietRecord record = new DietRecord();
            record.setFoodId(matchFood.getId());
            record.setFoodUnitId(unit.getId());
            record.setRecordUserId(requestDto.getUserId());
            record.setRecordTime(parseRecordTime(requestDto.getRecordTime()));
            record.setRecordValue(Math.max(1, (int) Math.round(fragment.count)));
            dietRecordMapper.insert(record);
            saved++;

            VoiceFoodItemDto voiceFoodItemDto = new VoiceFoodItemDto();
            voiceFoodItemDto.setFoodName(matchFood.getName());
            voiceFoodItemDto.setCount(record.getRecordValue());
            voiceFoodItemDto.setUnitName(unit.getUnitName());
            voiceFoodItemDto.setMatched(true);
            responseDto.getMatchedItems().add(voiceFoodItemDto);
        }

        responseDto.setSavedCount(saved);
        return responseDto;
    }

    private LocalDateTime parseRecordTime(String recordTime) {
        if (recordTime == null || recordTime.isEmpty()) {
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(recordTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception ignored) {
            return LocalDateTime.now();
        }
    }

    private List<VoiceFragment> parseVoiceText(String rawText) {
        List<VoiceFragment> fragments = new ArrayList<>();
        String[] split = rawText.replace('，', ',').replace('。', ',').replace('；', ',').replace('、', ',').split(",");
        for (String part : split) {
            String fragment = part == null ? "" : part.trim();
            if (fragment.isEmpty()) {
                continue;
            }
            VoiceFragment voiceFragment = new VoiceFragment();
            voiceFragment.sourceText = fragment;
            voiceFragment.normalizedText = normalizeText(fragment);
            voiceFragment.count = extractCount(fragment);
            voiceFragment.unitKeyword = extractUnitKeyword(fragment);
            fragments.add(voiceFragment);
        }
        if (fragments.isEmpty() && !rawText.isEmpty()) {
            VoiceFragment voiceFragment = new VoiceFragment();
            voiceFragment.sourceText = rawText;
            voiceFragment.normalizedText = normalizeText(rawText);
            voiceFragment.count = extractCount(rawText);
            voiceFragment.unitKeyword = extractUnitKeyword(rawText);
            fragments.add(voiceFragment);
        }
        return fragments;
    }

    private String normalizeText(String text) {
        return text
                .replace("吃了", "")
                .replace("喝了", "")
                .replace("我", "")
                .replace("今天", "")
                .replace("早餐", "")
                .replace("午餐", "")
                .replace("晚餐", "")
                .replace("加餐", "")
                .replace("记录", "")
                .replace("一下", "")
                .trim();
    }

    private double extractCount(String text) {
        Matcher matcher = ARABIC_COUNT_PATTERN.matcher(text);
        if (matcher.find()) {
            try {
                return Double.parseDouble(matcher.group(1));
            } catch (Exception ignored) {
            }
        }

        LinkedHashMap<String, Integer> chineseNumbers = new LinkedHashMap<>();
        chineseNumbers.put("两", 2);
        chineseNumbers.put("一", 1);
        chineseNumbers.put("二", 2);
        chineseNumbers.put("三", 3);
        chineseNumbers.put("四", 4);
        chineseNumbers.put("五", 5);
        chineseNumbers.put("六", 6);
        chineseNumbers.put("七", 7);
        chineseNumbers.put("八", 8);
        chineseNumbers.put("九", 9);
        chineseNumbers.put("十", 10);

        for (Map.Entry<String, Integer> entry : chineseNumbers.entrySet()) {
            if (text.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return 1;
    }

    private String extractUnitKeyword(String text) {
        Matcher matcher = ARABIC_COUNT_PATTERN.matcher(text);
        if (matcher.find() && matcher.group(2) != null) {
            return matcher.group(2).toLowerCase();
        }
        String[] units = {"个", "碗", "杯", "份", "克", "g", "ml", "毫升", "片", "块", "根", "只"};
        for (String unit : units) {
            if (text.toLowerCase().contains(unit)) {
                return unit;
            }
        }
        return "";
    }

    private Food findBestFoodMatch(String text, List<Food> foods) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        for (Food food : foods) {
            if (food.getName() != null && (text.contains(food.getName()) || food.getName().contains(text))) {
                return food;
            }
        }
        return null;
    }

    private FoodUnit chooseFoodUnit(Integer foodId, String unitKeyword) {
        List<FoodUnit> units = foodUnitMapper.selectList(new LambdaQueryWrapper<FoodUnit>()
                .eq(FoodUnit::getFoodId, foodId)
                .orderByAsc(FoodUnit::getId));
        if (units == null || units.isEmpty()) {
            return null;
        }
        if (unitKeyword == null || unitKeyword.isEmpty()) {
            return units.get(0);
        }
        for (FoodUnit unit : units) {
            if (unit.getUnitName() != null && unit.getUnitName().toLowerCase().contains(unitKeyword)) {
                return unit;
            }
        }
        return units.get(0);
    }

    private static class VoiceFragment {
        private String sourceText;
        private String normalizedText;
        private double count;
        private String unitKeyword;
    }
}
