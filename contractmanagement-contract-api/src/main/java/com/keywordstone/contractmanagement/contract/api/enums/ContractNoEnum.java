package com.keywordstone.contractmanagement.contract.api.enums;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 12:33 2018/5/7
 * @Modified By:
 */
public enum ContractNoEnum {

    //互联网支付合同
    ZF(1,"ZF"),
    //钱包商户合同
    QB(2,"QB"),
    //预付卡合同
    KJ(3,"KJ"),
    //跨境商户合同
    YF(4,"YF"),
    //渠道
    QD(5,"QD"),
    //其他
    QT(6,"QT");

    private int value;
    private String name;

    ContractNoEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static String getName(int index) {

        for (ContractNoEnum c : ContractNoEnum.values()) {
            if (c.getValue() == index) {
                return c.name;
            }
        }
        return null;
    }

    private int getValue() {
        return this.value;
    }

}

