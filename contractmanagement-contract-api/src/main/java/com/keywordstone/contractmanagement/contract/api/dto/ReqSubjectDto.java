package com.keywordstone.contractmanagement.contract.api.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReqSubjectDto extends AbstractDTO {
    private String id;
    private String name;
    private String parentId;
    private String basicRateId;
    private String memo;
    private String objectNo;
    private String rate;
    private BigDecimal minimumCharge;
}
