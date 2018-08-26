package com.keywordstone.contractmanagement.contract.api.boss;

import com.keywordstone.contractmanagement.usercenter.sdk.dto.CompanyMerchantDTO;
import com.keywordstone.framework.common.basic.dto.ResultDTO;

/**
 * 对接Boss接口
 *
 * @author SL
 * @date 2018/05/14
 */
public interface ButtBossService {

    /**
     * 查询客商信息
     *
     * @param params
     * @return
     */
    ResultDTO selecctCompanyInfo(ReqBossDTO params);

    /**
     * 插入关系表
     *
     * @param params
     * @return
     */
    ResultDTO addCompanyMerchant(ReqBossDTO params);
}
