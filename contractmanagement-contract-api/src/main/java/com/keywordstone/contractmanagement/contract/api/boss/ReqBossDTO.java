package com.keywordstone.contractmanagement.contract.api.boss;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

@Data
public class ReqBossDTO extends AbstractDTO {
    //协议编号
    private String contractNo;
    //商户号
    private String merchantNo;
}
