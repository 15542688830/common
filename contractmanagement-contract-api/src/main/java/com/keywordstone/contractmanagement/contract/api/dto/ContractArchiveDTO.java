package com.keywordstone.contractmanagement.contract.api.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 10:24 2018/3/14
 * @Modified By:
 */
@Data
public class ContractArchiveDTO extends AbstractDTO {

    //合同主键
    private String contractId;
    //商户主键
    private String firstPartyId;
    //乙方主键
    private String secondPartyId;
    //签约时间
    private String signTime;
    //合同名称
    private String contractName;
    //合同类型
    private String contractType;
    //归档用户主键
    private String archivedPersonId;
    //合同借阅状态(0:归档中;1:借阅中)
    private Integer contractStatus;
    //借阅用户主键
    private String borrowPersonId;
    //存放位置
    private String position;
    // 数据状态(0:最新数据;1:历史数据)
    private Integer dataStatus;
    //数据标志
    private Integer dataFlag;
    //创建时间
    private String createTime;
    //创建者
    private String createUser;
    //更新时间
    private String updateTime;
    //更新者
    private String updateUser;
}
