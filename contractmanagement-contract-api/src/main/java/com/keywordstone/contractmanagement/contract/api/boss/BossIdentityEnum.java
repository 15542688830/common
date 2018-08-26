package com.keywordstone.contractmanagement.contract.api.boss;

/**
 * 证件类型
 *
 * @author ChenChuang
 * @date 2018/2/23
 */
public enum BossIdentityEnum {
    /**
     * 身份证
     */
    IDENTITY(0, "IDC"),
    /**
     * 护照
     */
    PASSPORT(1, "PASS_PORT");

    private int value;
    private String name;

    BossIdentityEnum(int value, String name) {
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
