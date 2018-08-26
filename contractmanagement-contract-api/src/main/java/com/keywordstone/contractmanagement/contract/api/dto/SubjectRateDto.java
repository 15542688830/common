package com.keywordstone.contractmanagement.contract.api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class SubjectRateDto {
    private String id;
    private Integer verid;
    private String name;
    private Integer type;
    private String desp;
    private String parentId;
    private String parentName;
    private String basicRateId;
    private String objectNo;
    private String memo;
    private Integer dataFlag;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String updateUser;
    private String rate;
    private BigDecimal minimumCharge;
}
