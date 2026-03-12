package com.example.web.dto.query;

import com.example.web.tools.dto.PagedInput;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 收藏记录查询模型
 */
@NoArgsConstructor
@Data
public class CollectRecordPagedInput extends PagedInput {

    /**
     * 主键
     */
    @JsonProperty("Id")
    private Integer id;

    /**
     * 被收藏对象ID（文章ID / 食谱ID 等）
     */
    @JsonProperty("RelativeId")
    private Integer relativeId;

    /**
     * 收藏类型
     */
    @JsonProperty("CollectType")
    private String collectType;

    /**
     * 收藏人ID
     */
    @JsonProperty("CollectUserId")
    private Integer collectUserId;
}
