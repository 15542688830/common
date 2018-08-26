package com.keywordstone.contractmanagement.contract.sdk.enums;

/**
 * 文件类型
 *
 * @author ChenChuang
 * @date 2018/2/28
 */
public enum FileTypeEnum {

    /**
     * 营业执照
     */
    BUSINESSLICENSE(1, "营业执照"),
    /**
     * 开户许可证
     */
    ACCOUNTOPENINGPERMIT(2, "开户许可证"),
    /**
     * 法人身份证
     */
    LEGALCARD(3, "法人身份证"),
    /**
     * 双方盖章影印版
     */
    PHOTOCOPY(4, "双方盖章影印版"),
    /**
     * 领导特批证明
     */
    SPECIALPROVE(5, "领导特批证明"),
    /**
     * 补充协议
     */
    SUPPLEMENT(6, "补充协议"),
    /**
     * 合同主本
     */
    CONTRACT(7, "合同主本"),
    /**
     * 其他文件
     */
    OTHERS(8, "其他文件");
    private int value;
    private String name;

    FileTypeEnum(int value, String name) {
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
