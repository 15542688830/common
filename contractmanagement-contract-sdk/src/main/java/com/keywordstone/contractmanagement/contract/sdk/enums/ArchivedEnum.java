package com.keywordstone.contractmanagement.contract.sdk.enums;

/**
 * 文件类型
 *
 * @author ChenChuang
 * @date 2018/2/28
 */
public enum ArchivedEnum {

    /**
     * 可归档文件
     */
    Archived1(1, "可归档文件"),
    /**
     * 普通文件
     */
    Archived2(2, "普通文件");
    private int value;
    private String name;

    ArchivedEnum(int value, String name) {
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
