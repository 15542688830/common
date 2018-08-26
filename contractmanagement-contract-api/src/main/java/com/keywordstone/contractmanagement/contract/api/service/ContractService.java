package com.keywordstone.contractmanagement.contract.api.service;

import com.keywordstone.contractmanagement.contract.api.boss.ReqBossDTO;
import com.keywordstone.contractmanagement.contract.api.dto.ActionParamDTO;
import com.keywordstone.contractmanagement.contract.api.dto.ContractDTO;
import com.keywordstone.contractmanagement.contract.api.dto.HistoryContractDTO;
import com.keywordstone.framework.common.basic.dto.ResultDTO;

/**
 * 合同服务接口
 *
 * @author HJ
 * @date 2018/01/18
 */
public interface ContractService {
    /**
     * 新建合同
     *
     * @param contractDTO
     * @return
     */
    ResultDTO insertContract(ContractDTO contractDTO);

    ResultDTO insertHistoryContract(HistoryContractDTO historyContractDTO);

    ResultDTO editContract(ContractDTO contractDTO);

    ResultDTO selectOne(String id, String workId);

    ResultDTO selectContractById(String id);

    ResultDTO selectContractForBoss(ReqBossDTO reqBossDTO);

    ResultDTO actions(String id, String actionId,String entityId, ActionParamDTO actionParamDTO);

    ResultDTO viewPdf(String entityId);

    ResultDTO detailHistory(String entityId, int pageNo, int pageSize);

    ResultDTO getContractNo(String contractNo);

    String buildContractNo(ContractDTO contractDTO);

}
