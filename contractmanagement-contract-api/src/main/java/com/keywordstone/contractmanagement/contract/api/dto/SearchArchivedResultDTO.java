package com.keywordstone.contractmanagement.contract.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author: ZhangRui
 * @Description: 归档合同查询返回实体类
 * @date: Created in 13:57 2018/3/14
 * @Modified By:
 */
@Data
public class SearchArchivedResultDTO extends ContractArchiveDTO {

    //借阅任务ID
    private String taskId;
    //合同编号
    private String contractNo;
    //商户号
    private String partyNo;
    //商户名称
    private String partyName;
    //归档人名称
    private String archivedPersonName;
    //借阅用户主键
    private String borrowPersonName;
    //合同借阅状态
    private String contractStatusName;
    //借阅事由
    private String scopeUse;
    //归档时间
    private String createTime;
    //审核状态 1 通过  0 拒绝
    private String taskReview;
    //领导批示
    private String leaderOption;
    //借阅附件集合
    private List<String> fileArr;
    //申请日期
    private String applyDate;
    //借阅日期
    private String borrowDate;
    //归还状态 0 未归还 1 已归还
    private String returnStatus;
    //归还日期
    private String returnDate;
    //预计归还日期
    private String expectDate;
    //到期提醒日期
    private String dueDate;
    //备注
    private String memo;
    //存放位置
    private String position;

}
