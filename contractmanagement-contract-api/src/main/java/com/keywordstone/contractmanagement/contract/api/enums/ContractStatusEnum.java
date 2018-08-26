package com.keywordstone.contractmanagement.contract.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 12:33 2018/3/15
 * @Modified By:
 */
public enum ContractStatusEnum {
    ARCHIVED(0, "已归档"),
    BORROW(1, "借阅中");

    private int value;
    private String name;

    private ContractStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}

