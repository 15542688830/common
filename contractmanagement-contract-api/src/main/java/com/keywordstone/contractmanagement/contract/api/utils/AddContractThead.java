package com.keywordstone.contractmanagement.contract.api.utils;

import com.keywordstone.contractmanagement.contract.api.dto.ContractDTO;
import com.keywordstone.contractmanagement.contract.api.service.FileDespService;
import com.keywordstone.contractmanagement.contract.api.service.FileService;
import com.keywordstone.contractmanagement.contract.sdk.dto.ReqFileDto;
import com.keywordstone.contractmanagement.contract.sdk.enums.FileTypeEnum;
import com.keywordstone.contractmanagement.contract.sdk.enums.InvoiceTypeEnum;
import com.keywordstone.contractmanagement.file.sdk.dto.FileByteDto;
import com.keywordstone.contractmanagement.file.sdk.service.FileSdkService;
import com.keywordstone.contractmanagement.usercenter.sdk.dto.CompanyInfoDTO;
import com.keywordstone.contractmanagement.usercenter.sdk.service.CompanyInfoSdkService;
import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.common.database.mybatis.entity.DContModel;
import com.keywordstone.framework.common.database.mybatis.service.DContModelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class AddContractThead {

    @Value("${file.server}")
    private String fileServer;

    @Value("${libre.com.win}")
    private String libre_com_win;

    @Value("${libre.com.lux}")
    private String libre_com_lux;

    @Autowired
    private CompanyInfoSdkService companyInfoSdkService;
    @Autowired
    private DContModelService dContModelService;
    @Autowired
    private FileDespService fileDespService;
    @Autowired
    private FileSdkService fileSdkService;
    @Autowired
    private FileService fileService;
    /**
     * 最简单的异步调用，返回值为void
     */
    @Async
    public void asyncInvokeSimplest() {
        log.info("asyncSimplest");
    }

    /**
     * 带参数的异步调用 异步方法可以传入参数
     *
     * @param contractId
     * @param contractDTO
     */
    //另起线程注解
    @Async
    public ResultDTO pdfAction(String contractId, ContractDTO contractDTO) {
        try {
            log.info("ContractDTO参数：" + contractDTO.toString());
            // 把模板下载到本地
            ResultDTO<Map<String, Object>> resultDTO = downloadFromFileServer(contractDTO.getModelId(), contractDTO.getContractName());
            String srcPath = resultDTO.getData().get("contFile").toString(); // 合同模板docx路径+文件名
            String descPath = resultDTO.getData().get("newLocal").toString(); // 新生成的docx合同路径+文件名
            String pdfLocal = resultDTO.getData().get("pdfLocal").toString(); // 转换后pdf文件路径
            String pdfName = resultDTO.getData().get("pdfName").toString(); // PDF文件名

            File fileModel = new File(srcPath);

            if (!fileModel.exists()) {
                log.info("模板文件下载失败");
                return ResultDTO.failure();
            }

            Map<String, String> map = new HashMap<>();
            map.put("${manageDetailAddress}", ""); // 经营地址
            map.put("${postCode}", ""); // 邮编
            map.put("${firstparty}", ""); // 甲方客户
            map.put("${legalName}", ""); // 法定代表人
            map.put("${tradeType}", ""); // 行业分类
            map.put("${companyTel}", ""); // 电话
            map.put("${companyFax}", ""); // 传真
            map.put("${accountName}", ""); // 开户名
            map.put("${bankName}", ""); // 开户行
            map.put("${bankAccount}", ""); // 开户账号
            map.put("${swiftCode}", ""); // 联行号
            map.put("${companyMail}", ""); // E-mail（官方指定信息收发邮箱）
            map.put("${companyContract}", ""); // 业务联系人
            map.put("${companyContractTel}", ""); // 业务联系人手机
            map.put("${companyContracMail}", ""); // 业务联系人E-mail
            map.put("${companyContracQQ}", ""); // 业务联系人qq活MSN
            map.put("${bizno}", ""); // 商户编号
            map.put("${invoiceType}", ""); // 发票性质

            /* 新增字段 */
            map.put("${specialName}", ""); // 银行存管专用账户开户名
            map.put("${specialBank}", ""); // 银行存管专用账户开户行
            map.put("${specialAccount}", ""); // 银行存管专用账户开户账号
            map.put("${specialSwiftCode}", ""); // 银行存管专用账户联行号
            map.put("${fundsName}", ""); // 自有资金账户开户名
            map.put("${fundsBank}", ""); // 自有资金账户开户行
            map.put("${fundsAccount}", ""); // 自有资金账户开户账号
            map.put("${fundsSwiftCode}", ""); // 自有资金账户联行号

            /*  新增字段-跨境支付  */
            map.put("${recordsNo}", ""); // 海关备案号
            map.put("${workingTime}", ""); // 客服工作时间
            map.put("${serviceTel}", ""); // 客服热线
            map.put("${serviceQQ}", ""); // 客服联系人QQ
            map.put("${serviceName}", ""); // 客服联系人姓名
            map.put("${technicalQQ}", ""); // 技术联系人MSN/QQ
            map.put("${technicalEmail}", ""); // 技术联系人Email
            map.put("${technicalMobile}", ""); // 技术联系人手机
            map.put("${technicalName}", ""); // 技术联系人姓名
            map.put("${adminQQ}", ""); // 管理员QQ
            map.put("${adminEmail}", ""); // 管理员Email
            map.put("${adminMobile}", ""); // 管理员手机
            map.put("${adminName}", ""); // 管理员姓名
            map.put("${tradeType}", ""); // 行业分类
            map.put("${bankPCDAddress}", ""); // 开户行地址
            map.put("${swiptAba}", ""); // SWIFT号或FW/ABA清算号
            map.put("${accountCountry}", ""); // 收款账户国别
            map.put("${currency}", ""); // 所属区域币种
            map.put("${accountName}", ""); // 收款账户名称
            map.put("${bankAccount}", ""); // 开户账户

            map.put("${firstpayContrac}", ""); // 乙方业务联系人
            map.put("${firstpayContracTel}", ""); // 乙方业务联系人手机
            map.put("${firstpayContracMail}", ""); // 乙方业务联系人E-mail
            map.put("${firstpayContracQQ}", ""); // 乙方业务联系人MSN/QQ

            ResultDTO<CompanyInfoDTO> resParty1; //  通过商户ID查询商户信息
            ResultDTO<CompanyInfoDTO> resParty2; //  查询企业信息

            if (contractDTO != null && contractDTO.getFirstPartyInfo() != null
                    && contractDTO.getFirstPartyInfo().getId() != null
                    && "" != contractDTO.getFirstPartyInfo().getId()) {
                log.info("甲方companyId: " + contractDTO.getFirstPartyInfo().getId());
                resParty1 = companyInfoSdkService.getCompUnitByCompId(contractDTO.getFirstPartyInfo().getId());
                if (resParty1 != null && resParty1.getData() != null) {
                    // 法定代表人

                    map.put("${manageDetailAddress}", resParty1.getData().getManageDetailAddress() == null ? "" : resParty1.getData().getManageDetailAddress()); // 经营地址
                    map.put("${bizno}", resParty1.getData().getBizNo() == null ? "" : resParty1.getData().getBizNo()); // 商户编号
                    map.put("${postCode}", resParty1.getData().getPostCode() == null ? "" : resParty1.getData().getPostCode()); // 邮编
                    map.put("${firstparty}", resParty1.getData().getComName() == null ? "" : resParty1.getData().getComName()); // 甲方客户
                    map.put("${legalName}", resParty1.getData().getLegalName() == null ? "" : resParty1.getData().getLegalName()); // 法定代表人
                    map.put("${tradeType}", resParty1.getData().getTradeType() == null ? "" : resParty1.getData().getTradeType()); // 行业分类
                    map.put("${companyTel}", resParty1.getData().getCompanyTel() == null ? "" : resParty1.getData().getCompanyTel()); // 电话
                    map.put("${companyFax}", resParty1.getData().getCompanyFax() == null ? "" : resParty1.getData().getCompanyFax()); // 传真
                    map.put("${accountName}", resParty1.getData().getAccountName() == null ? "" : resParty1.getData().getAccountName()); // 开户名
                    map.put("${bankName}", resParty1.getData().getBankName() == null ? "" : resParty1.getData().getBankName()); // 开户行
                    map.put("${bankAccount}", resParty1.getData().getBankAccount() == null ? "" : resParty1.getData().getBankAccount()); // 开户账号
                    map.put("${swiftCode}", resParty1.getData().getSwiftCode() == null ? "" : resParty1.getData().getSwiftCode()); // 联行号
                    map.put("${companyMail}", resParty1.getData().getCompanyMail() == null ? "" : resParty1.getData().getCompanyMail()); // E-mail（官方指定信息收发邮箱）
                    map.put("${companyContract}", resParty1.getData().getContactName() == null ? "" : resParty1.getData().getContactName()); // 业务联系人
                    map.put("${companyContractTel}", resParty1.getData().getContactMobile() == null ? "" : resParty1.getData().getContactMobile()); // 业务联系人手机
                    map.put("${companyContracMail}", resParty1.getData().getContactEmail() == null ? "" : resParty1.getData().getContactEmail()); // 业务联系人E-mail
                    map.put("${companyContracQQ}", resParty1.getData().getQq() == null ? "" : resParty1.getData().getQq()); // 业务联系人qq活MSN

                    /*  新增字段-资金存管  */
                    map.put("${specialName}", resParty1.getData().getSpecialName() == null ? "" : resParty1.getData().getSpecialName()); // 银行存管专用账户开户名
                    map.put("${specialBank}", resParty1.getData().getSpecialBank() == null ? "" : resParty1.getData().getSpecialBank()); // 银行存管专用账户开户行
                    map.put("${specialAccount}", resParty1.getData().getSpecialAccount() == null ? "" : resParty1.getData().getSpecialAccount()); // 银行存管专用账户开户账号
                    map.put("${specialSwiftCode}", resParty1.getData().getSpecialSwiftCode() == null ? "" : resParty1.getData().getSpecialSwiftCode()); // 银行存管专用账户联行号
                    map.put("${fundsName}", resParty1.getData().getFundsName() == null ? "" : resParty1.getData().getFundsName()); // 自有资金账户开户名
                    map.put("${fundsBank}", resParty1.getData().getFundsBank() == null ? "" : resParty1.getData().getFundsBank()); // 自有资金账户开户行
                    map.put("${fundsAccount}", resParty1.getData().getFundsAccount() == null ? "" : resParty1.getData().getFundsAccount()); // 自有资金账户开户账号
                    map.put("${fundsSwiftCode}", resParty1.getData().getFundsSwiftCode() == null ? "" : resParty1.getData().getFundsSwiftCode()); // 自有资金账户联行号

                    /*  新增字段-跨境支付  */
                    map.put("${recordsNo}", resParty1.getData().getRecordsNo() == null ? "" : resParty1.getData().getRecordsNo()); // 海关备案号
                    map.put("${workingTime}", resParty1.getData().getWorkingTime() == null ? "" : resParty1.getData().getWorkingTime()); // 客服工作时间
                    map.put("${serviceTel}", resParty1.getData().getServiceTel() == null ? "" : resParty1.getData().getServiceTel()); // 客服热线
                    map.put("${serviceQQ}", resParty1.getData().getServiceQQ() == null ? "" : resParty1.getData().getServiceQQ()); // 客服联系人QQ
                    map.put("${serviceName}", resParty1.getData().getServiceName() == null ? "" : resParty1.getData().getServiceName()); // 客服联系人姓名
                    map.put("${technicalQQ}", resParty1.getData().getTechnicalQQ() == null ? "" : resParty1.getData().getTechnicalQQ()); // 技术联系人MSN/QQ
                    map.put("${technicalEmail}", resParty1.getData().getTechnicalEmail() == null ? "" : resParty1.getData().getTechnicalEmail()); // 技术联系人Email
                    map.put("${technicalMobile}", resParty1.getData().getTechnicalMobile() == null ? "" : resParty1.getData().getTechnicalMobile()); // 技术联系人手机
                    map.put("${technicalName}", resParty1.getData().getTechnicalName() == null ? "" : resParty1.getData().getTechnicalName()); // 技术联系人姓名
                    map.put("${adminQQ}", resParty1.getData().getAdminQQ() == null ? "" : resParty1.getData().getAdminQQ()); // 管理员QQ
                    map.put("${adminEmail}", resParty1.getData().getAdminEmail() == null ? "" : resParty1.getData().getAdminEmail()); // 管理员Email
                    map.put("${adminMobile}", resParty1.getData().getAdminMobile() == null ? "" : resParty1.getData().getAdminMobile()); // 管理员手机
                    map.put("${adminName}", resParty1.getData().getAdminName() == null ? "" : resParty1.getData().getAdminName()); // 管理员姓名
                    map.put("${tradeType}", resParty1.getData().getTradeType() == null ? "" : resParty1.getData().getTradeType()); // 行业分类
                    String bankPCDAddress = "";
                    if (StringUtils.isNotBlank(resParty1.getData().getBankProvince()) ||
                            StringUtils.isNotBlank(resParty1.getData().getBankCity()) ||
                            StringUtils.isNotBlank(resParty1.getData().getBankDistrict()) ||
                            StringUtils.isNotBlank(resParty1.getData().getBankAddress())) {
                        bankPCDAddress = resParty1.getData().getBankProvince() + resParty1.getData().getBankCity() + resParty1.getData().getBankDistrict() + resParty1.getData().getBankAddress();
                        map.put("${bankPCDAddress}", bankPCDAddress); // 开户行地址
                    }
                    map.put("${swiptAba}", resParty1.getData().getSwiptAba() == null ? "" : resParty1.getData().getSwiptAba()); // SWIFT号或FW/ABA清算号
                    map.put("${accountCountry}", resParty1.getData().getAccountCountry() == null ? "" : resParty1.getData().getAccountCountry()); // 收款账户国别
                    map.put("${currency}", resParty1.getData().getCurrency() == null ? "" : resParty1.getData().getCurrency()); // 所属区域币种
                    map.put("${accountName}", resParty1.getData().getAccountName() == null ? "" : resParty1.getData().getAccountName()); // 收款账户名称
                    map.put("${bankAccount}", resParty1.getData().getBankAccount() == null ? "" : resParty1.getData().getBankAccount()); // 开户账户
                }
            }
            // 通过用户ID查询乙方信息
            if (contractDTO != null && contractDTO.getSecondPartyInfo() != null
                    && StringUtils.isNotBlank(contractDTO.getSecondPartyInfo().getId())) {

                log.info("乙方companyId: " + contractDTO.getSecondPartyInfo().getId());
                resParty2 = companyInfoSdkService.getCompUnitByCompId(contractDTO.getSecondPartyInfo().getId());

                if (resParty2 != null && resParty2.getData() != null) {
                    map.put("${firstpayContrac}", resParty2.getData().getContactName() == null ? "" : resParty2.getData().getContactName()); // 乙方业务联系人
                    map.put("${firstpayContracTel}", resParty2.getData().getContactMobile() == null ? "" : resParty2.getData().getContactMobile()); // 乙方业务联系人手机
                    map.put("${firstpayContracMail}", resParty2.getData().getContactEmail() == null ? "" : resParty2.getData().getContactEmail()); // 乙方业务联系人E-mail
                    map.put("${firstpayContracQQ}", resParty2.getData().getQq() == null ? "" : resParty2.getData().getQq()); // 乙方业务联系人MSN/QQ
                }
            }


            map.put("${contractno}", contractDTO.getCoverInfo().getContractNo() == null ? "" : contractDTO.getCoverInfo().getContractNo()); // 协议编号
            map.put("${firstpartyproductname}", contractDTO.getContractDetailInfo().getFirstPartyProductName() == null ? "" : contractDTO.getContractDetailInfo().getFirstPartyProductName());// 甲方网址或APP名
            map.put("${firstPartyProductDesp}", contractDTO.getContractDetailInfo().getFirstPartyProductDesp() == null ? "" : contractDTO.getContractDetailInfo().getFirstPartyProductDesp()); // 甲方经营业务描述 经营内容
            map.put("${serviceopeningfee}", contractDTO.getContractDetailInfo().getServiceOpeningFee() == null ? "" : contractDTO.getContractDetailInfo().getServiceOpeningFee()); // 服务开通费
            map.put("${feecollectiontype}", contractDTO.getContractDetailInfo().getFeeCollectionType() == null ? "" : contractDTO.getContractDetailInfo().getFeeCollectionType()); // 手续费收取方式
            map.put("${invoiceopeningfrequency}", contractDTO.getContractDetailInfo().getInvoiceOpeningFrequency() == null ? "" : contractDTO.getContractDetailInfo().getInvoiceOpeningFrequency()); // 发票开具频率
            map.put("${bail}", contractDTO.getContractDetailInfo().getBail() == null ? "" : contractDTO.getContractDetailInfo().getBail()); // 保证金
            map.put("${commitmentbusinessuse}", contractDTO.getContractDetailInfo().getCommitmentBusinessUse() == null ? "" : contractDTO.getContractDetailInfo().getCommitmentBusinessUse()); // 承诺业务用途
            map.put("${firstpartyreportemail}", contractDTO.getContractDetailInfo().getFirstPartyReportEmail() == null ? "" : contractDTO.getContractDetailInfo().getFirstPartyReportEmail()); // 甲方举报邮箱
            map.put("${getdoccontact}", contractDTO.getContractDetailInfo().getGetDocContact() == null ? "" : contractDTO.getContractDetailInfo().getGetDocContact()); // 甲方调单联系人
            map.put("${phone}", contractDTO.getContractDetailInfo().getPhone() == null ? "" : contractDTO.getContractDetailInfo().getPhone()); //电话
            map.put("${fax}", contractDTO.getContractDetailInfo().getFax() == null ? "" : contractDTO.getContractDetailInfo().getFax()); // 传真
            map.put("${email}", contractDTO.getContractDetailInfo().getEmail() == null ? "" : contractDTO.getContractDetailInfo().getEmail()); // 邮箱
            if (null != contractDTO.getContractDetailInfo().getInvoiceType()) {
                if (contractDTO.getContractDetailInfo().getInvoiceType() == InvoiceTypeEnum.SERVICENATURE.getValue()) {
                    map.put("${invoiceType}", InvoiceTypeEnum.SERVICENATURE.getName()); // 发票性质
                }
            }

            // 签约日期
            String signDate = "";
            String year = ""; // 例：签约日期2018/03/27 截取出【8】
            String month = ""; // 例：签约日期2018/03/27 截取出【03】
            String day = ""; // 例：签约日期2018/03/27 截取出【27】
            String yearA = ""; // 例：签约日期2018/03/27 截取出【2018】
            String yearANew = ""; // 签约日期+1年时间
            String monthNew = ""; // 签约日期+1年时间
            String dayNew = ""; // 签约日期+1年时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            Date date = new Date();
            if (contractDTO.getCoverInfo().getSignTime() != null && !"".equals(contractDTO.getCoverInfo().getSignTime())) {

                signDate = contractDTO.getCoverInfo().getSignTime();
                if (signDate.contains("-")) {
                    signDate = signDate.replace("-", "/");
                }
                year = signDate.substring(3, 4);
                month = signDate.substring(5, 7);
                day = signDate.substring(8, 10);
                yearA = signDate.substring(0, 4);
                // 获取签约日期+1年时间
                date = sdf.parse(signDate);
                Calendar curr = Calendar.getInstance();
                curr.setTime(date);
                curr.set(Calendar.YEAR, curr.get(Calendar.YEAR) + 1);
                String dateNewString = sdf.format(curr.getTime());
                yearANew = dateNewString.substring(0, 4);
                monthNew = dateNewString.substring(5, 7);
                dayNew = dateNewString.substring(8, 10);
            }

            map.put("${year}", year);
            map.put("${month}", month);
            map.put("${day}", day);
            map.put("${yearA}", yearA);
            map.put("${yearANew}", yearANew);
            map.put("${monthNew}", monthNew);
            map.put("${dayNew}", dayNew);

            // 循环获取已选择的标的-服务内容
            // 如果是新生成的合同
            if (contractDTO.getContStatus().equals("insert")) {
                for (HashMap<String, Object> subMap : contractDTO.getContractSubjectList()) {
                    if (subMap.get("basicDataFlag") != null && subMap.get("basicDataFlag").toString().equals("true")) {
                        String ser = "";
                        String rate = "";
                        String min = "";
                        if (subMap.get("id") != null && !"".equals(subMap.get("id"))) {
                            ser = subMap.get("id").toString();
                            map.put("${" + ser + "}", "开通");
                        }
                        if (subMap.get("rate") != null && !"".equals(subMap.get("rate"))) {
                            rate = subMap.get("rate").toString();
                            map.put("${" + ser + "rate}", rate);
                        }
                        if (subMap.get("minimumCharge") != null && !"".equals(subMap.get("minimumCharge"))) {
                            min = subMap.get("minimumCharge").toString();
                            map.put("${" + ser + "min}", min);
                        }
                    }
                }
            } else if (contractDTO.getContStatus().equals("edit")) {
                for (HashMap<String, Object> subMap : contractDTO.getContractSubjectList()) {
                    if (subMap.get("basicDataFlag") != null && subMap.get("basicDataFlag").toString().equals("true") || subMap.get("basicDataFlag") == null) {
                        String ser = "";
                        String rate = "";
                        String min = "";
                        if (subMap.get("baseObjectId") != null && !"".equals(subMap.get("baseObjectId"))) {
                            ser = subMap.get("baseObjectId").toString();
                            map.put("${" + ser + "}", "开通");
                        }
                        if (subMap.get("rate") != null && !"".equals(subMap.get("rate"))) {
                            rate = subMap.get("rate").toString();
                            map.put("${" + ser + "rate}", rate);
                        }
                        if (subMap.get("minimumCharge") != null && !"".equals(subMap.get("minimumCharge"))) {
                            min = subMap.get("minimumCharge").toString();
                            map.put("${" + ser + "min}", min);
                        }
                    }
                }
            }


            // 往模板中填充数据
            File file = new File(pdfLocal);
            if (!file.exists()) {
                file.mkdir();
            }
            ResultDTO replaceResult = searchAndReplace(srcPath, descPath, map);

            if (replaceResult != null && replaceResult.getCode() == ResultDTO.SUCCESS) {

                log.info(contractDTO.getContractName() + "===将填充好的合同文件转换成PDF格式 libreoffice命令转换pdf:" + new Date());
                // 将填充好的合同文件转换成PDF格式 libreoffice命令转换pdf
                String osName = System.getProperty("os.name");
                String command = "";
                if (osName.contains("Windows")) {
                    command = libre_com_win;
                } else {
                    command = libre_com_lux;
                }
                long start = System.currentTimeMillis();
                CommandExecute.wordConverterToPdf(command, descPath, pdfLocal);
                File pdfFile = new File(pdfLocal + File.separator + pdfName);
                if (pdfFile.exists()) {
                    long end = System.currentTimeMillis();
                    log.info(contractDTO.getContractName() + "==转换成功，用时：" + (end - start) + "ms");
                    log.info("==pdf文件存放位置：" + pdfLocal);
                } else {
                    log.info("PDF转换失败");
                }


                // 删除模板文件
                boolean b = fileModel.delete();
                if (b) {
                    log.info("模板文件删除成功");
                } else {
                    log.info("模板文件删除失败");
                }

                // 上传PDF文件
                ResultDTO uploadResult = uploadPdf(pdfLocal, contractDTO.getContractName() + ".pdf", contractId, contractDTO, descPath);

            } else {
                ResultDTO.failure();
            }

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            ResultDTO.failure();
        }

        return ResultDTO.success();
    }

    // 从文件服务器下载模板文件
    public ResultDTO downloadFromFileServer(String modelId, String contractName) {

        log.info("获取模板文件path...");
        Map<String, Object> map = new HashMap<>();

        // 通过模板ID查询对应的文件ID
        ResultDTO<DContModel> modelResultDTO = null;
        if (modelId != null && "" != modelId) {
            modelResultDTO = dContModelService.selectByPrimaryKey(modelId);
        }

        // 通过文件ID获得文件path
        ResultDTO<ReqFileDto> fileResultDTO = null;
        if (modelResultDTO != null && modelResultDTO.getData() != null && modelResultDTO.getData().getModelUrl() != null) {
            String fileId = modelResultDTO.getData().getModelUrl();
            try {
                fileResultDTO = fileService.selecctFileByid(fileId);
            } catch (Exception e) {
                log.info("获取模板文件path失败");
                log.error(e.getMessage(), e);
                return ResultDTO.failure();
            }

        } else {
            log.info("获取模板文件path失败");
            return ResultDTO.failure();
        }

        // 拼接模板所在文件服务器的位置路径
        String filePath = "";
        String fileName = ""; // 文件名称
        if (fileResultDTO != null && fileResultDTO.getData() != null && fileResultDTO.getData().getFilePath() != null) {
            log.info("获取模板文件path成功");
            filePath = fileResultDTO.getData().getFilePath();
            filePath = filePath.substring(filePath.indexOf("/"), filePath.length()); // 文件path去除group，用于拼接url
            fileName = filePath.substring(filePath.lastIndexOf("/") + 1); // docx文件名称

        }
        // 文件所在服务器的URL地址
        String targetUrl = fileServer + filePath;

        // docx模板的存储路径
        String storagePath = System.getProperty("java.io.tmpdir") + File.separator;

        // 把模板下载到本地
        ResultDTO downFileResult = downFile(storagePath, targetUrl, fileName);

        if (downFileResult != null && downFileResult.getCode() == ResultDTO.SUCCESS) {

            map.put("contFile", storagePath + fileName);
            map.put("newLocal", storagePath + "contract" + File.separator + contractName + ".docx");
            map.put("pdfLocal", storagePath + "contract");
            map.put("fileName", fileName);
            map.put("pdfName", contractName + ".pdf");
            return ResultDTO.success(map);

        } else {
            return ResultDTO.failure();
        }


    }

    /**
     * 上传PDF文件并生成file表数据
     *
     * @param pdfLocal
     * @param pdfName
     * @return
     * @throws Exception
     */
    public ResultDTO uploadPdf(String pdfLocal, String pdfName, String contractId, ContractDTO contractDTO, String descPath) throws Exception {

        log.info("上传合同pdf文件到服务器开始...");
        long start = System.currentTimeMillis();

        ResultDTO<Map<String, Object>> pdfMap = null;
        File filePdf = new File(pdfLocal + File.separator + pdfName);
        ResultDTO resFile = null;
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        ByteArrayOutputStream baos = null;
        BufferedOutputStream bout = null;
        try {
            fin = new FileInputStream(filePdf);
            bin = new BufferedInputStream(fin);
            baos = new ByteArrayOutputStream();
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while (len != -1) {
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }
            bout.flush();
            byte[] bytes = baos.toByteArray();
            FileByteDto fileByteDto = new FileByteDto();
            fileByteDto.setBuff(bytes);
            fileByteDto.setFileName(pdfName);
            pdfMap = fileSdkService.uploadByteFile(fileByteDto);


        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            log.info("上传合同pdf文件到服务器失败");
            return ResultDTO.failure();
        } finally {
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                log.info("上传合同pdf文件到服务器失败");
                return ResultDTO.failure();
            }
        }
//        // 上传成功后删除pdf文件和docx文件
//        filePdf.delete();
//        File filedocx = new File(descPath);
//        filedocx.delete();

        // filedesp表和file表插入数据
        if (pdfMap != null && pdfMap.getCode() == ResultDTO.SUCCESS) {
            long end = System.currentTimeMillis();
            log.info("上传成功，用时" + (end - start) + "ms");
            log.info("插入file和filedesp表开始...");
            ReqFileDto reqFileDto = new ReqFileDto();
            reqFileDto.setCountId(contractId);
            if (pdfMap.getData().get("url") != null) {
                reqFileDto.setFilePath(pdfMap.getData().get("url").toString());
            }
            reqFileDto.setFileName(pdfName);
            reqFileDto.setDesp(contractDTO.getContractDesc());
            reqFileDto.setType(FileTypeEnum.CONTRACT.getValue());
//            reqFileDto.setCountType(contractDTO.getContType());
            reqFileDto.setFileNo("contractMain");
            try {
                resFile = fileService.delFileByCondition(reqFileDto);
                resFile = fileDespService.insertFileDesp(reqFileDto);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                log.info("插入file和filedesp表开始失败");
                return ResultDTO.failure();
            }
        } else {
            log.info("插入file和filedesp表开始失败");
            return ResultDTO.failure();
        }

        if (resFile != null && resFile.getCode() == ResultDTO.SUCCESS) {
            log.info("插入file和filedesp表成功");
            return ResultDTO.success();
        } else {
            log.info("插入file和filedesp表失败");
            return ResultDTO.failure();
        }
    }

    /**
     * 从文件服务器上下载文件到本地
     *
     * @param storagePath 模板本地存放路径
     * @param targetUrl   文件服务器的地址路径
     * @param fileName    文件名称
     */
    public ResultDTO downFile(String storagePath, String targetUrl, String fileName) {
        log.info("从服务器下载模板文件开始...");
        long start = System.currentTimeMillis();
        File file = new File(storagePath);
        if (!file.exists()) {
            file.mkdir();
        }
        FileOutputStream fileOut;
        HttpURLConnection conn;
        InputStream inputStream;
        try {
            URL httpUrl = new URL(targetUrl);
            conn = (HttpURLConnection) httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.connect();
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            fileOut = new FileOutputStream(storagePath + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);
            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.flush();
            bos.close();
            fileOut.close();
            bis.close();
            inputStream.close();
            conn.disconnect();


        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            log.info("从服务器下载模板文件失败");
            return ResultDTO.failure();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            log.info("从服务器下载模板文件失败");
            return ResultDTO.failure();
        }
        long end = System.currentTimeMillis();
        log.info("从服务器下载模板文件结束 用时：" + (end - start) + "ms");

        return ResultDTO.success();
    }

    public ResultDTO searchAndReplace(String srcPath, String destPath, Map<String, String> map) {
        log.info("替换模板文件开始...");
        long start = System.currentTimeMillis();

        try {
            XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(srcPath));
            /**
             * 替换段落中的指定文字
             */
            // 循环段落list
            Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();

            while (itPara.hasNext()) {
                XWPFParagraph paragraph = (XWPFParagraph) itPara.next();

                int length = paragraph.getRuns().size();
                // 段落开始的字体名称和字体大小
                int fontSize = 14;
                String fontName = "华文细黑";
                boolean bold = false;

                if (length > 0) {
                    for (int i = 0; i <= length - 1; i++) {
//                        log.info(String.valueOf(paragraph.getRuns().get(i).getFontSize()));
//                        log.info(paragraph.getRuns().get(i).getFontName());
                        if (StringUtils.isNotBlank(paragraph.getRuns().get(i).getText(0))) {
                            // 获得有内容的runs的第一个字体
                            fontSize = paragraph.getRuns().get(i).getFontSize();
                            fontName = paragraph.getRuns().get(i).getFontName();
                            bold = paragraph.getRuns().get(i).isBold();
                            break;
                        }

                    }
                    String text = StringUtils.join(paragraph.getRuns().toArray());
                    if (text.contains("乙双方的基本信息和账户信息如下：")) {
                        paragraph.setPageBreak(true);
                    }
                    if (text.indexOf("$") < 0) {
                        continue;
                    }
                    for (int i = (length - 1); i >= 0; i--) {

                        paragraph.removeRun(i);
                    }

                    XWPFRun newRun = paragraph.insertNewRun(0);
//                    paragraph.setSpacingLineRule(LineSpacingRule.AUTO);

                    for (String key : map.keySet()) {
                        if (text.contains(key)) {
                            text = text.replace(key, map.get(key));
                        }
                    }
                    newRun.setFontSize(fontSize);
                    newRun.setFontFamily(fontName);
                    newRun.setBold(bold);
                    newRun.setText(text, 0);
//                    newRun.setUnderline(UnderlinePatterns.SINGLE);

                }

            }

            /**
             * 替换表格中的指定文字
             */
            /**
             * 1 页面标的部分 银联二维码扫码需要选中借记卡或者信用卡
             * 2 页面标的部分 单笔金额<= 1000 和 单笔金额 > 1000 对应的minimumCharge值要一样
             * 4 页面表格部分 填写模板参数的时候把除了参数之外的空格去除掉
             * 5 页面标的部分 是以DB中d_cont_basic_object对应表的主键关联选中或者删除，不要更改db对应的标的的主键
             */
            Iterator<XWPFTable> itTable = document.getTablesIterator();
            while (itTable.hasNext()) {
                XWPFTable table = (XWPFTable) itTable.next();
                int count = table.getNumberOfRows();
                for (int i = 0; i < count; i++) {
                    XWPFTableRow row = table.getRow(i);
                    List<XWPFTableCell> cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {
                        // 如果表格不为空
                        if (cell.getText().length() > 0) {
                            String cellString = cell.getText();
                            int len = strTimes(cellString, "$");
                            boolean b = false;

                            // 一个单元格里有多个需要插入的参数
                            if (len > 1) {
                                for (String key : map.keySet()) {
                                    if (cellString.contains(key)) {
                                        cellString = cellString.replace(key, map.get(key));
                                    }
                                }
                                // 清除一个单元格中包含多个参数，但并没有被赋值的参数名称
                                if (cellString.contains("$")) {
                                    int len1 = strTimes(cellString, "$");
                                    for (int n = 1; n <= len1; n++) {
                                        String newString = cellString.substring(cellString.indexOf("${"), cellString.indexOf("}") + 1);
                                        cellString = cellString.replace(newString, "");
                                    }
                                }
                                cell.removeParagraph(0);
                                cell.setText(cellString);
                                cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);

                            } else {
                                // 一个单元格只有一个参数
                                for (Map.Entry<String, String> e : map.entrySet()) {
                                    if (cell.getText().equals(e.getKey())) {
                                        cell.removeParagraph(0);
                                        cell.setText(e.getValue());
                                        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
                                        b = true;
                                    }
                                }
                                // 清除标的表格中没有选择的项目
                                if (b == false && cellString.contains("$")) {
                                    cell.removeParagraph(0);
                                    cell.setText("");
                                }
                            }
                        } else {


                        }
                    }
                }
            }
            FileOutputStream outStream = null;
            outStream = new FileOutputStream(destPath);
            document.write(outStream);
            outStream.close();
            document.close();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            log.info("替换模板文件失败");
            return ResultDTO.failure();
        }

        long end = System.currentTimeMillis();
        log.info("替换成功模板文件：" + destPath);
        log.info("替换模板文件结束 用时：" + (end - start) + "ms");
        return ResultDTO.success();

    }

    /**
     * 判断固定字符在字符串中出现的次数
     *
     * @param str 原始字符串
     * @param tag 出现多次的字符串
     * @return
     */
    private int strTimes(String str, String tag) {

        return str.length() - str.replace(tag, "").length();
    }

}
