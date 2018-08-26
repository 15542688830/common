package com.keywordstone.contractmanagement.contract.api.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 14:12 2018/5/18
 * @Modified By:
 */
@Data
public class BossProductResultDTO extends AbstractDTO{

        /**
         * 高级产品编号
         */
        private String complexNo;
        /**
         * 高级产品名称
         */
        private String complexName;
        /**
         * 业务编号
         */
        private String bizCode;
        /**
         * 访问模式
         */
        private String accessMode;
        /**
         * 产品类型
         */
        private String complexType;
        /**
         * 支付模式
         */
        private String payMode;
        /**
         * 对公标记
         */
        private String ispublicFlag;
        /**
         * 金额标记
         */
        private String amountFlag;
        /**
         * 业务模式
         */
        private String businessMode;
        /**
         * 业务提交模式
         */
        private String buscommitMode;
        /**
         * 预留字段1
         */
        private String reserve1;
        /**
         * 预留字段2
         */
        private String reserve2;
        /**
         * 计费关联表code
         */
        private String feeCode;

        private long gmtDate;
        //  页码
        private int pageNo = 1;

        //  每页条数
        private int eachPageNum = 1000;

        //销售产品
        private String objectName;

        //是否已绑定 0 未关联  1 已关联
        private Integer alreadyContact;
}
