package com.keywordstone.contractmanagement.contract.sdk.enums;

public enum AgreeTypeEnum {


    MAIN(1, "主协议"),

    SUPPLEMENT(2, "补充协议");

    private int value;
    private String name;

    AgreeTypeEnum(int value, String name) {
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
