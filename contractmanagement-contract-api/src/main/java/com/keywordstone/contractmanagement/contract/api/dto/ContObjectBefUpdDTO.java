package com.keywordstone.contractmanagement.contract.api.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author sunyue
 * @create 2018-05-14 14:36
 **/
@Data
public class ContObjectBefUpdDTO {
    private String id;
    private String baseObjectName;
    private String baseObjectId;
    private Integer basicDataFlag;
    private String parentId;
    private String parentName;
    private String rate;
    private BigDecimal minimumCharge;
}
