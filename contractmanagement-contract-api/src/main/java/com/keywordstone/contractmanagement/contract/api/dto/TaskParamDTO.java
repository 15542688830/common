package com.keywordstone.contractmanagement.contract.api.dto;

import lombok.Data;

@Data
public class TaskParamDTO {

    private Integer pageNo;

    private Integer pageSize;

    private Integer taskStatus;
}
