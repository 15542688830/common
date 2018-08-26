package com.keywordstone.contractmanagement.contract.api.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 8:30 2018/3/2
 * @Modified By:
 */
@Data
public class SearchResultDTO extends AbstractDTO {
    //合同名称
    private String contractName;
    //当前执行人
    private String executorName;
    //合同状态
    private String contractStatus;
    //合同类型
    private String contractType;
    //合同文件标记
    private String contractFileFlag;
    //发起人
    private String originator;
    //工作流主键
    private String taskId;
    //创建时间
    private String createTime;
    // 页面是否显示补充协议 值为2 不显示，值为1 显示
    private String agreeType;
    // parentComId
    private String parentComid;
    // 商户名称
    private String comName;
}
