package com.example.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.web.dto.VoiceFoodItemDto;
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
import java.util.*;

@Service
public class VoiceServiceImpl implements VoiceService {

    @Autowired
    private FoodMapper foodMapper;

    @Autowired
    private FoodUnitMapper foodUnitMapper;

    @Autowired
    private DietRecordMapper dietRecordMapper;

    @Override
    public VoiceRecognizeResponseDto recognizeAndSave(Integer userId, MultipartFile file) {

        // ⭐ 模拟语音识别结果
        String text = "我今天吃了两碗米饭和一个鸡蛋";

        List<VoiceFoodItemDto> items = parseFood(text);

        for (VoiceFoodItemDto item : items) {

            Food food = foodMapper.selectOne(
                    new LambdaQueryWrapper<Food>()
                            .eq(Food::getName, item.getFoodName())
                            .last("LIMIT 1")
            );

            if (food == null) continue;

            FoodUnit unit = foodUnitMapper.selectOne(
                    new LambdaQueryWrapper<FoodUnit>()
                            .eq(FoodUnit::getFoodId, food.getId())
                            .last("LIMIT 1")
            );

            if (unit == null) continue;

            DietRecord record = new DietRecord();
            record.setFoodId(food.getId());
            record.setFoodUnitId(unit.getId());
            record.setRecordUserId(userId);
            record.setRecordValue(item.getCount());
            record.setRecordTime(LocalDateTime.now());

            dietRecordMapper.insert(record);
        }

        VoiceRecognizeResponseDto dto = new VoiceRecognizeResponseDto();
        dto.setText(text);

        return dto;
    }

    // ⭐ 语义解析
    private List<VoiceFoodItemDto> parseFood(String text) {

        List<VoiceFoodItemDto> list = new ArrayList<>();

        Map<String, Integer> numberMap = new HashMap<>();
        numberMap.put("一", 1);
        numberMap.put("两", 2);
        numberMap.put("二", 2);
        numberMap.put("三", 3);

        if (text.contains("米饭")) {
            VoiceFoodItemDto dto = new VoiceFoodItemDto();
            dto.setFoodName("米饭");
            dto.setCount(getCount(text, numberMap));
            list.add(dto);
        }

        if (text.contains("鸡蛋")) {
            VoiceFoodItemDto dto = new VoiceFoodItemDto();
            dto.setFoodName("鸡蛋");
            dto.setCount(getCount(text, numberMap));
            list.add(dto);
        }

        return list;
    }

    private Integer getCount(String text, Map<String,Integer> numberMap){
        for(String key : numberMap.keySet()){
            if(text.contains(key)){
                return numberMap.get(key);
            }
        }
        return 1;
    }
}
