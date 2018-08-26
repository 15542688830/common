package com.keywordstone.contractmanagement.contract.api.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskResultDTO {

    // 合同ID
    private String entityId;
    // 合同名称
    private String entityName;
    // 发起人
    private String createUser;
    // 发起时间
    private Date createTime;
    // 合同类型
    private String contType;
    // 当前流程
    private String layer;
    // 当前执行人
    private String operUser;
    // 工作任务
    private String workTask;
    // 商户名称
    private String operateKey;

    private String taskId;

    private String createTimeString;

}
