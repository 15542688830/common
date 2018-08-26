package com.keywordstone.contractmanagement.contract.api.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

/**
 * @Author: ZhangRui
 * @Description: 产品配置查询返回值实体类
 * @date: Created in 14:59 2018/3/19
 * @Modified By:
 */
@Data
public class SearchProductResultDTO extends AbstractDTO {

    //合同主键
    private String contractId;
    //合同名称
    private String contractName;
    //公司名称
    private String companyName;
    //发起人
    private String originator;
    //合同状态
    private String contractStatus;
    //合同类型
    private String contractType;

    @Data
    public class Product {

        //销售产品主键
        private String objectId;
        //销售父产品主键
        private String parentId;
        //销售产品父分类
        private String objectParent;
        //销售产品子分类
        private String objectChild;
        //销售产品费率
        private String objectRate;
        //销售产品最低收费
        private String objectMinCharge;
        //配置产品主键
        private String productId;
        //配置产品编号
        private String productNo;
        //配置产品名称
        private String productName;
        //销售产品费率
        private String productRate;
        //配置产品最低收费
        private String productMinCharge;
        //配置产品与配置产品关联状态
        private Integer status;
        //配置产品开通状态 未开通 已开通
        private String openStatus;

    }
}
