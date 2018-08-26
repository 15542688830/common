package com.keywordstone.contractmanagement.contract.api.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 10:48 2018/5/18
 * @Modified By:
 */
@Data
public class ConfigProductDTO extends AbstractDTO{

    //销售产品主键
    private String objectId;
    //配置产品编号
    private String productNo;
    //配置产品名称
    private String name;
    //销售产品费率
    private BigDecimal rate;
    //配置产品最低收费
    private BigDecimal minimumCharge;
    //配置产品与配置产品关联状态
    private Integer status;
}
