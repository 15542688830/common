package com.keywordstone.contractmanagement.contract.api.boss;

/**
 * 商户内外部分类
 *
 * @author ChenChuang
 * @date 2018/2/23
 * @return
 */
public enum BossComInsideTypeEnum {

    /**
     * 内部
     */
    INSIDE(0, "IN"),
    /**
     * 外部
     */
    EXTERNAL(1, "OUT"),
    /**
     * 系统用户
     */
    SYSTEM(2, "SYS");


    private int value;
    private String name;

    BossComInsideTypeEnum(int value, String name) {
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
