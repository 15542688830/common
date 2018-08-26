package com.keywordstone.contractmanagement.contract.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author sunyue
 * @create 2018-05-11 14:59
 **/
@Data
public class ContractBefUpdDTO {

    //合同主键
    private String id;
    //模板类型
    private Integer modelType;
    //模板类型名称
    private String modelTypeName;
    //合同类型
    private Integer contType;
    //合同类型名称
    private String contTypeName;
    //合同名称
    private String name;
    //合同描述
    private String desp;
    //合同编号
    private String contractNo;
    //合同签约地点
    private String signAt;
    //合同签约时间
    private String signTime;
    //甲方网址或App名
    private String firstPartyProductName;
    //甲方经营业务描述
    private String firstPartyProductDesp;
    //服务开通费
    private String serviceOpeningFee;
    //手续费收取方式
    private String feeCollectionType;
    //发票开具频率
    private String invoiceOpeningFrequency;
    //保证金
    private String bail;
    //承诺业务用途
    private String commitmentBusinessUse;
    //甲方举报邮箱
    private String firstPartyReportEmail;
    //调单联系人
    private String getDocContact;
    //电话
    private String phone;
    //传真
    private String fax;
    //E-mail
    private String email;
    //甲方公司主键
    private String firstPartyId;
    // 甲方公司名称
    private String comName;
    // 商户编号
    private String bizNo;
    // 法人代表姓名
    private String legalName;
    // 实际经营地址-省
    private String manageProvince;
    // 实际经营地址-市
    private String manageCity;
    // 实际经营地址-区
    private String manageDistrict;
    // 开户名
    private String accountName;
    // 开户行
    private String bankName;
    // 开户账户
    private String bankAccount;
    // 联行号
    private String swiftCode;
    //乙方公司主键
    private String secondPartyId;
    // 乙方公司名称
    private String secondPartyComName;
    // 标的List
    private List<ContObjectBefUpdDTO> dContObjectList;
    // 发票性质
    private String invoiceType;
}
