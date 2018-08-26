package com.keywordstone.contractmanagement.contract.api.dto;

import lombok.Data;

@Data
public class ModelParamDTO {
    // 页码
    private Integer pageNumber;
    // 每页的数据量
    private Integer pageSize;
    // 合同类型
    private Integer contStatus;
    // 模板分类
    private Integer modelStatus;
    // 删除状态 0:正常状态 1:删除  2:作废
    private Integer dataFlag;
    // 模板名称
    private String modelName;
    // File表名称
    private String fileName;
    // 模板描述
    private String modelDesp;
    // 模板对应的file表ID
    private String modelUrl;
    // 模板ID
    private String modelId;


}
