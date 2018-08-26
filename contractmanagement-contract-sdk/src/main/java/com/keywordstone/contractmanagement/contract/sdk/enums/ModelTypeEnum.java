package com.keywordstone.contractmanagement.contract.sdk.enums;

public enum ModelTypeEnum {

    /**
     * 标准模板
     */
    STANDARD(1, "标准模板"),
    /**
     * 非标准模板
     */
    NONSTANDARD(2, "非标准模板"),
    /**
     * 无模板
     */
    NONMODEL(10, "无模板");

    private int value;
    private String name;

    ModelTypeEnum(int value, String name) {
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
