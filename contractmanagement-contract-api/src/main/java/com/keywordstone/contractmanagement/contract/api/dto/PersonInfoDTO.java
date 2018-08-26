package com.keywordstone.contractmanagement.contract.api.dto;

import lombok.Data;

/**
 * 人员信息
 *
 * @author ChenChuang
 * @date 2018/3/5
 */
@Data
public class PersonInfoDTO {
    /**
     * 人员姓名
     */
    private String personName;
    /**
     * 人员部门
     */
    private String personDepartment;
}
