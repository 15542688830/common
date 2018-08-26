package com.keywordstone.contractmanagement.contract.api.service;

import com.keywordstone.contractmanagement.contract.api.dto.ReqSubjectDto;
import com.keywordstone.framework.common.basic.dto.ResultDTO;

/**
 * 基础费率服务接口
 *
 * @author SL
 * @date 2018/01/18
 */
public interface BasicRateService {
    /**
     * 新建基础费率
     *
     * @param params
     * @return
     */
    ResultDTO insertBasicRate(ReqSubjectDto params);

    /**
     * 编辑基础费率
     *
     * @param params
     * @return
     */
    ResultDTO updateBasicRate(ReqSubjectDto params);

    /**
     * 查询基础费率list
     *
     * @param params
     * @return
     */
    ResultDTO selecctBasicRate(ReqSubjectDto params);

    /**
     * 查询基础费率byid
     *
     * @param id
     * @return
     */
    ResultDTO selecctBasicRatebyid(String id);

    /**
     * 删除基础费率
     *
     * @param params
     * @return
     */
    ResultDTO delBasicRate(ReqSubjectDto params);
}
