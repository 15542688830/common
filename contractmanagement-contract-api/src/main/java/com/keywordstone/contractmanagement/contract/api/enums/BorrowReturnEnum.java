package com.keywordstone.contractmanagement.contract.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 12:33 2018/4/11
 * @Modified By:
 */
public enum BorrowReturnEnum {

    UNRETURN("0", "未归还"),
    RETURN("1", "已归还");

    private String value;
    private String name;

    BorrowReturnEnum(String value, String name) {
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

