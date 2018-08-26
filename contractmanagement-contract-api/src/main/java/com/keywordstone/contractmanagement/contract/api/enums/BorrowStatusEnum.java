package com.keywordstone.contractmanagement.contract.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 12:33 2018/4/11
 * @Modified By:
 */
public enum BorrowStatusEnum {

    REFUSE("0", "拒绝"),
    ADOPT("1", "通过"),
    WAIT("2", "待审核"),
    FINISH("3", "已归还");

    private String value;
    private String name;

    BorrowStatusEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}

