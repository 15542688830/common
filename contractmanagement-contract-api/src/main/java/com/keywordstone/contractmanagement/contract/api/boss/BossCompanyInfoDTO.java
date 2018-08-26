package com.keywordstone.contractmanagement.contract.api.boss;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BossCompanyInfoDTO {
    /**
     * 企业ID
     */
    private String id;
    /**
     * 商户名称
     */
    private String comName;
    /**
     * 商户英文缩写
     */
    private String nameAbbreviation;
    /**
     * 商户编号
     */
    private String bizNo;
    /**
     * 商户属性
     */
    private Integer bizProperty;
    /**
     * 海关备案号
     */
    private String recordsNo;
    /**
     * 境内外标识
     */
    private Integer domesticType;
    /**
     * 境外所属国家或地区
     */
    private String countryArea;
    /**
     * 商户内外部分类
     */
    private String comInsideType;
    /**
     * 业务分类
     */
    private Integer bizType;
    /**
     * 商户行业
     */
    private String tradeType;
    /**
     * 1级商户行业
     */
    private String tradeTypeParent;
    /**
     * 公司章程
     */
    private String comRule;
    /**
     * 公司章程文件名称
     */
    private String comRuleName;
    /**
     * 公司章程文件ID
     */
    private String comRuleID;
    /**
     * 行业补充资质
     */
    private String tradeUrl;
    /**
     * 行业补充资质URL1
     */
    private String tradeUrl0;
    /**
     * 行业补充资质URL2
     */
    private String tradeUrl1;
    /**
     * 行业补充资质URL3
     */
    private String tradeUrl2;
    /**
     * 行业补充资质名称1
     */
    private String tradeUrlName0;
    /**
     * 行业补充资质名称2
     */
    private String tradeUrlName1;
    /**
     * 行业补充资质名称3
     */
    private String tradeUrlName2;
    /**
     * 行业补充资质文件ID1
     */
    private String tradeUrlID0;
    /**
     * 行业补充资质文件ID2
     */
    private String tradeUrlID1;
    /**
     * 行业补充资质文件ID3
     */
    private String tradeUrlID2;
    /**
     * 行业补充资质补充附件
     */
    private String tradeExtraUrl;
    /**
     * 社会统一信用代码
     */
    private String orgCode;
    /**
     * 营业执照注册日期
     */
    private String licenseRegDate;
    /**
     * 营业执照到期日期
     */
    private String licenseExpiryDate;
    /**
     * 经营范围
     */
    private String bizDesp;
    /**
     * 注册资本金
     */
    private BigDecimal regfund;
    /**
     * 注册资本金币种
     */
    private Integer regfundCy;
    /**
     * 实缴资本金
     */
    private BigDecimal paidCapital;
    /**
     * 实缴资本金币种
     */
    private Integer paidCapitalCy;
    /**
     * 注册地址
     */
    private String regAddress;
    /**
     * 注册详细地址
     */
    private String regDetailAddress;
    /**
     * 注册地址-省
     */
    private String regProvince;
    /**
     * 注册地址-市
     */
    private String regCity;
    /**
     * 注册地址-区
     */
    private String regDistrict;
    /**
     * 实际经营地址
     */
    private String manageAddress;
    /**
     * 实际经营详细地址
     */
    private String manageDetailAddress;
    /**
     * 实际经营地址-省
     */
    private String manageProvince;
    /**
     * 实际经营地址-市
     */
    private String manageCity;
    /**
     * 实际经营地址-区
     */
    private String manageDistrict;
    /**
     * 企业邮编
     */
    private String postCode;
    /**
     * 企业邮箱
     */
    private String companyMail;
    /**
     * 企业传真
     */
    private String companyFax;
    /**
     * 企业联系电话
     */
    private String companyTel;
    /**
     * 营业执照图片
     */
    private String licenseIUrl;
    /**
     * 营业执照图片名称
     */
    private String licenseUrlName;
    /**
     * 营业执照图片ID
     */
    private String licenseUrlID;
    /**
     * 银行开户名称
     */
    private String accountName;
    /**
     * 开户行
     */
    private String bankName;
    /**
     * 开户账户
     */
    private String bankAccount;
    /**
     * 结算银行
     */
    private String bankSettlement;
    /**
     * 联行号
     */
    private String swiftCode;
    /**
     * 银行开户许可证
     */
    private String openingAccountsPermitsUrl;
    /**
     * 银行开户许可证文件名称
     */
    private String openingAccountsPermitsUrlName;
    /**
     * 银行开户许可证文件ID
     */
    private String openingAccountsPermitsUrlID;
    /**
     * 银行存管专用账户开户名
     */
    private String specialName;
    /**
     * 银行存管专用账户开户行
     */
    private String specialBank;
    /**
     * 银行存管专用账户开户账号
     */
    private String specialAccount;
    /**
     * 银行存管专用账户联行号
     */
    private String specialSwiftCode;
    /**
     * 自有资金账户开户名
     */
    private String fundsName;
    /**
     * 自有资金账户开户行
     */
    private String fundsBank;
    /**
     * 自有资金账户开户账号
     */
    private String fundsAccount;
    /**
     * 自有资金账户联行号
     */
    private String fundsSwiftCode;
    /**
     * 开户行地址-省
     */
    private String bankProvince;
    /**
     * 开户行地址-市
     */
    private String bankCity;
    /**
     * 开户行地址-区
     */
    private String bankDistrict;
    /**
     * 开户行地址省市区
     */
    private String bankPCD;
    /**
     * 开户行详细地址
     */
    private String bankAddress;
    /**
     * SWIPT号或FW/ABA清算号
     */
    private String swiptAba;
    /**
     * 所属区域币种
     */
    private String currency;
    /**
     * 收款账户国别
     */
    private String accountCountry;
    /**
     * 法人代表姓名
     */
    private String legalName;
    /**
     * 法人身份证件类型
     */
    private String legalIdentityType;
    /**
     * 法人证件号码
     */
    private String legalIdentityId;
    /**
     * 法人证件到期日期
     */
    private String legalIdentityDate;
    /**
     * 法人证件签发地
     */
    private String legalCreAddress;
    /**
     * 法人证件签发地详细地址
     */
    private String legalCreDetailAddress;
    /**
     * 法人证件签发地-省
     */
    private String legalProvince;
    /**
     * 法人证件签发地-市
     */
    private String legalCity;
    /**
     * 法人证件签发地-区
     */
    private String legalDistrict;
    /**
     * 法人证件照片
     */
    private String legalIdentityIUrl;
    /**
     * 法人证件照片名称
     */
    private String legalIdentityUrlName;
    /**
     * 法人证件照片ID
     */
    private String legalIdentityUrlID;
    /**
     * 实际控制人姓名
     */
    private String realityName;
    /**
     * 实际控制人身份证件类型
     */
    private String realityIdentityType;
    /**
     * 实际控制人证件号码
     */
    private String realityIdentityId;
    /**
     * 实际控制人证件到期日期
     */
    private String realityIdentityDate;
    /**
     * 实际控制人证件签发地
     */
    private String realityCreAddress;
    /**
     * 实际控制人证件签发地详细地址
     */
    private String realityCreDetailAddress;
    /**
     * 实际控制人证件签发地-省
     */
    private String realityProvince;
    /**
     * 实际控制人证件签发地-市
     */
    private String realityCity;
    /**
     * 实际控制人证件签发地-区
     */
    private String realityDistrict;
    /**
     * 实际控制人证件照片
     */
    private String realityIdentityIUrl;
    /**
     * 实际控制人证件照片名称
     */
    private String realityIdentityUrlName;
    /**
     * 实际控制人证件照片ID
     */
    private String realityIdentityUrlID;
    /**
     * 申请人姓名
     */
    private String applyName;
    /**
     * 申请人身份证件类型
     */
    private String applyIdentityType;
    /**
     * 申请人证件号码
     */
    private String applyIdentityId;
    /**
     * 申请人证件到期日期
     */
    private String applyIdentityDate;
    /**
     * 申请人证件签发地
     */
    private String applyCreAddress;
    /**
     * 申请人证件签发地详细地址
     */
    private String applyCreDetailAddress;
    /**
     * 申请人证件签发地-省
     */
    private String applyProvince;
    /**
     * 申请人证件签发地-市
     */
    private String applyCity;
    /**
     * 申请人证件签发地-区
     */
    private String applyDistrict;
    /**
     * 申请人证件照片
     */
    private String applyIdentityIUrl;
    /**
     * 申请人证件照片名称
     */
    private String applyIdentityUrlName;
    /**
     * 申请人证件照片ID
     */
    private String applyIdentityUrlID;
    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 联系人手机
     */
    private String contactMobile;
    /**
     * 联系人邮箱
     */
    private String contactEmail;
    /**
     * 管理员姓名
     */
    private String adminName;
    /**
     * 管理员手机
     */
    private String adminMobile;
    /**
     * 管理员Email
     */
    private String adminEmail;
    /**
     * 管理员QQ
     */
    private String adminQQ;
    /**
     * 技术联系人姓名
     */
    private String technicalName;
    /**
     * 技术联系人手机
     */
    private String technicalMobile;
    /**
     * 技术联系人Email
     */
    private String technicalEmail;
    /**
     * 技术联系人QQ
     */
    private String technicalQQ;
    /**
     * 客服联系人姓名
     */
    private String serviceName;
    /**
     * 客服联系人QQ
     */
    private String serviceQQ;
    /**
     * 客服热线
     */
    private String serviceTel;
    /**
     * 客服工作时间
     */
    private String workingTime;
    /**
     * 产品ID
     */
    private String productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * QQ
     */
    private String qq;
}
