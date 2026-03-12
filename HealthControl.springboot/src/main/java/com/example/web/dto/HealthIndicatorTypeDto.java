package com.example.web.dto;

import com.example.web.tools.dto.BaseDto;
import com.example.web.entity.HealthIndicatorType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 健康指标分类 DTO
 */
@Data
public class HealthIndicatorTypeDto extends BaseDto {

    /**
     * 分类名称
     */
    @JsonProperty("Name")
    private String Name;

    /**
     * 所属用户 ID
     */
    @JsonProperty("BelongUserId")
    private Integer BelongUserId;

    /**
     * 是否公用
     */
    @JsonProperty("IsComm")
    private Boolean IsComm;

    /**
     * 所属用户 DTO
     */
    @JsonProperty("BelongUserDto")
    private AppUserDto BelongUserDto;

    /**
     * 该分类下的健康指标列表（⭐新增字段，解决编译错误）
     */
    @JsonProperty("HealthIndicatorList")
    private List<HealthIndicatorDto> healthIndicatorDtoList;

    /**
     * DTO → Entity
     */
    public HealthIndicatorType MapToEntity()
            throws InvocationTargetException, IllegalAccessException {

        HealthIndicatorType entity = new HealthIndicatorType();
        BeanUtils.copyProperties(entity, this);
        return entity;
    }
}
