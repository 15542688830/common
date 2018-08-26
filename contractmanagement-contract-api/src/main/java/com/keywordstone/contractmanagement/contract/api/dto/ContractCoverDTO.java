package com.keywordstone.contractmanagement.contract.api.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

/**
 * 合同封面
 *
 * @author HJ
 * @date 2018-01-25
 */
@Data
public class ContractCoverDTO extends AbstractDTO {
    private String signAt;
    private String signTime;
    private String contractNo;

}
