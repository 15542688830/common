package com.keywordstone.contractmanagement.contract.api.utils;

import com.keywordstone.contractmanagement.contract.api.dto.HistoryContractDTO;
import com.keywordstone.contractmanagement.contract.sdk.enums.ContractTypeEnum;
import com.keywordstone.contractmanagement.usercenter.sdk.dto.CompanyInfoDTO;
import com.keywordstone.contractmanagement.usercenter.sdk.enums.*;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImportExecl {


    /**
     * 总行数
     */
    private int totalRows = 0;
    /**
     * 总列数
     */
    private int totalCells = 0;
    /**
     * 错误信息
     */
    private String errorInfo;

    /**
     * 构造方法
     */
    public ImportExecl() {
    }


    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalCells() {
        return totalCells;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public boolean validateExcel(File file, String filePath) {
        /** 检查文件名是否为空或者是否是Excel格式的文件 */
        if (filePath == null
                || !(WDWUtil.isExcel2003(filePath) || WDWUtil
                .isExcel2007(filePath))) {
            errorInfo = "文件名不是excel格式";
            return false;
        }
        /** 检查文件是否存在 */
        if (file == null || !file.exists()) {
            errorInfo = "文件不存在";
            return false;
        }
        return true;
    }

    public List<List<String>> read(File file, String filePath) {
        List<List<String>> dataLst = new ArrayList<List<String>>();
        InputStream is = null;
        try {
            /** 验证文件是否合法 */
            if (!validateExcel(file, filePath)) {
                System.out.println(errorInfo);
                return null;
            }
            /** 判断文件的类型，是2003还是2007 */
            boolean isExcel2003 = true;
            if (WDWUtil.isExcel2007(filePath)) {
                isExcel2003 = false;
            }
            /** 调用本类提供的根据流读取的方法 */
            is = new FileInputStream(file);
            dataLst = read(is, isExcel2003);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        /** 返回最后读取的结果 */
        return dataLst;
    }


    public List<List<String>> read(InputStream inputStream, boolean isExcel2003) {
        List<List<String>> dataLst = null;
        try {
            /** 根据版本选择创建Workbook的方式 */
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }
            dataLst = read(wb);
        } catch (IOException e) {

            e.printStackTrace();
        }
        return dataLst;
    }


    private List<List<String>> read(Workbook wb) {
        List<List<String>> dataLst = new ArrayList<List<String>>();
        /** 得到第一个shell */
        Sheet sheet = wb.getSheetAt(0);
        /** 得到Excel的行数 */
        this.totalRows = sheet.getPhysicalNumberOfRows();
        /** 得到Excel的列数 */
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }
        /** 循环Excel的行 */
        for (int r = 0; r < this.totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            List<String> rowLst = new ArrayList<String>();
            /** 循环Excel的列 */
            for (int c = 0; c < this.getTotalCells(); c++) {
                Cell cell = row.getCell(c);
                String cellValue = "";
                if (null != cell) {
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                            // 以下是判断数据的类型
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                Date d = cell.getDateCellValue();
                                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                                cellValue = formater.format(d);
                            } else {
                                cellValue = String.valueOf(cell.getNumericCellValue() + "");
                            }
                            break;
                        case HSSFCell.CELL_TYPE_STRING: // 字符串
                            cellValue = cell.getStringCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                            cellValue = cell.getBooleanCellValue() + "";
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA: // 公式
                            cellValue = cell.getCellFormula() + "";
                            break;
                        case HSSFCell.CELL_TYPE_BLANK: // 空值
                            cellValue = "";
                            break;
                        case HSSFCell.CELL_TYPE_ERROR: // 故障
                            cellValue = "非法字符";
                            break;
                        default:
                            cellValue = "未知类型";
                            break;
                    }
                }
                rowLst.add(cellValue);
            }
            /** 保存第r行的第c列 */
            dataLst.add(rowLst);
        }
        return dataLst;
    }

    /**
     * 读取Excel导入客商
     *
     * @param companyInfoStringList
     * @return
     */
    public List<CompanyInfoDTO> readExcelToCompany(List<List<String>> companyInfoStringList) throws ParseException {
        List<CompanyInfoDTO> companyInfoDTOS = new ArrayList<CompanyInfoDTO>();
        Map<String, Integer> bizmap = getBizmap();
        Map<String, Integer> paidcymap = getPaidtalCy();
        Map<String, Integer> identitymap = getIdentity();
        if (companyInfoStringList != null) {
            //"第" + (n) + "行"
            for (int n = 1; n < companyInfoStringList.size(); n++) {
                List<String> cellList = companyInfoStringList.get(n);
                //"第" + (i) + "列"
                CompanyInfoDTO companyInfoDTO = new CompanyInfoDTO();
                int i = 0;
                if (StringUtils.isNotEmpty(cellList.get(i))) {
                    companyInfoDTO.setComName(WDWUtil.ran2fuc(cellList.get(i)));
                    //商户属性 - 转换
                    if (StringUtils.isNotEmpty(cellList.get(i + 1))) {
                        String bizPropety = WDWUtil.ran2fuc(cellList.get(i + 1));
                        int type = BizPropertyEnum.NETWORK.getValue();
                        if (BizPropertyEnum.NETWORK.getName().equals(bizPropety)) {
                            type = BizPropertyEnum.NETWORK.getValue();
                        } else if (BizPropertyEnum.ENTITY.getName().equals(bizPropety)) {
                            type = BizPropertyEnum.ENTITY.getValue();
                        } else if (BizPropertyEnum.ENTITYANDNETWORK.getName().equals(bizPropety)) {
                            type = BizPropertyEnum.ENTITYANDNETWORK.getValue();
                        }
                        companyInfoDTO.setBizProperty(type);
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 2))) {
                        String domesticType = WDWUtil.ran2fuc(cellList.get(i + 2));
                        int type = DomesticTypeEnum.CHURCHYARD.getValue();
                        if (DomesticTypeEnum.CHURCHYARD.getName().equals(domesticType)) {
                            type = DomesticTypeEnum.CHURCHYARD.getValue();
                        } else if (DomesticTypeEnum.OVERSEAS.getName().equals(domesticType)) {
                            type = DomesticTypeEnum.OVERSEAS.getValue();
                        }
                        companyInfoDTO.setDomesticType(type);
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 3)))
                        companyInfoDTO.setCountryArea(WDWUtil.ran2fuc(cellList.get(i + 3)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 4))) {
                        String comInsideType = WDWUtil.ran2fuc(cellList.get(i + 4));
                        int type = ComInsideTypeEnum.INSIDE.getValue();
                        if (ComInsideTypeEnum.INSIDE.getName().equals(comInsideType)) {
                            type = ComInsideTypeEnum.INSIDE.getValue();
                        } else if (ComInsideTypeEnum.EXTERNAL.getName().equals(comInsideType)) {
                            type = ComInsideTypeEnum.EXTERNAL.getValue();
                        } else if (ComInsideTypeEnum.SYSTEM.getName().equals(comInsideType)) {
                            type = ComInsideTypeEnum.SYSTEM.getValue();
                        }
                        companyInfoDTO.setComInsideType(type);
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 5))) {
                        int biztype = bizmap.get(WDWUtil.ran2fuc(cellList.get(i + 5)));
                        companyInfoDTO.setBizType(biztype);
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 6))) {
                        companyInfoDTO.setTradeType(WDWUtil.ran2fuc(cellList.get(i + 6)));
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 7)))
                        companyInfoDTO.setTradeType(WDWUtil.ran2fuc(cellList.get(i + 7)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 8))){
                        companyInfoDTO.setTradeType(WDWUtil.ran2fuc(cellList.get(i + 8)));
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 9))) {
                        companyInfoDTO.setRecordsNo(WDWUtil.ran2fuc(cellList.get(i + 9)));
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 10)))
                        companyInfoDTO.setComRule(WDWUtil.ran2fuc(cellList.get(i + 10)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 11)))
                        companyInfoDTO.setTradeUrl(WDWUtil.ran2fuc(cellList.get(i + 11)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 12)))
                        companyInfoDTO.setTradeUrl0(WDWUtil.ran2fuc(cellList.get(i + 12)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 13)))
                        companyInfoDTO.setOrgCode(WDWUtil.ran2fuc(cellList.get(i + 13)).trim());
                    if (StringUtils.isNotEmpty(cellList.get(i + 14)))
                        companyInfoDTO.setLicenseRegDate(WDWUtil.ran2fuc(cellList.get(i + 14)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 15))) {
                        String licenseExpiryDate = WDWUtil.ran2fuc(cellList.get(i + 15));
                        if (!licenseExpiryDate.equals("长期"))
                            companyInfoDTO.setLicenseExpiryDate(licenseExpiryDate);
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 16)))
                        companyInfoDTO.setBizDesp(WDWUtil.ran2fuc(cellList.get(i + 16)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 17))) {
                        String gundStr = WDWUtil.ran2fuc(cellList.get(i + 17));
                        if(StringUtils.isNotEmpty(gundStr)){
                            if("/t".equals(gundStr.substring(0,1))){
                                gundStr = gundStr.substring(2);
                            }
                            BigDecimal gund = new BigDecimal(gundStr);
                            companyInfoDTO.setRegfund(gund);
                        }
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 18))){
                        int regfundCy = paidcymap.get(WDWUtil.ran2fuc(cellList.get(i + 18)));
                        companyInfoDTO.setRegfundCy(regfundCy);
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 19))){
                        if(StringUtils.isNotEmpty(WDWUtil.ran2fuc(cellList.get(i + 19)))){
                            BigDecimal gund = new BigDecimal(WDWUtil.ran2fuc(cellList.get(i + 19)));
                            companyInfoDTO.setPaidCapital(gund);
                        }
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 20))){
                        int regfundCy = paidcymap.get(WDWUtil.ran2fuc(cellList.get(i + 20)));
                        companyInfoDTO.setPaidCapitalCy(regfundCy);
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 21)))
                        companyInfoDTO.setRegAddress(WDWUtil.ran2fuc(cellList.get(i + 21)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 22)))
                        companyInfoDTO.setRegDetailAddress(WDWUtil.ran2fuc(cellList.get(i + 22)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 23)))
                        companyInfoDTO.setManageAddress(WDWUtil.ran2fuc(cellList.get(i + 23)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 24)))
                        companyInfoDTO.setManageDetailAddress(WDWUtil.ran2fuc(cellList.get(i + 24)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 25)))
                        companyInfoDTO.setPostCode(WDWUtil.ran2fuc(cellList.get(i + 25)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 26)))
                        companyInfoDTO.setCompanyMail(WDWUtil.ran2fuc(cellList.get(i + 26)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 27)))
                        companyInfoDTO.setCompanyFax(WDWUtil.ran2fuc(cellList.get(i + 27)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 28)))
                        companyInfoDTO.setCompanyTel(WDWUtil.ran2fuc(cellList.get(i + 28)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 29)))
                        companyInfoDTO.setLicenseIUrl(WDWUtil.ran2fuc(cellList.get(i + 29)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 30)))
                        companyInfoDTO.setBankSettlement(WDWUtil.ran2fuc(cellList.get(i + 30)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 31)))
                        companyInfoDTO.setAccountName(WDWUtil.ran2fuc(cellList.get(i + 31)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 32)))
                        companyInfoDTO.setBankName(WDWUtil.ran2fuc(cellList.get(i + 32)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 33)))
                        companyInfoDTO.setBankAccount(WDWUtil.ran2fuc(cellList.get(i + 33)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 34)))
                        companyInfoDTO.setSwiftCode(WDWUtil.ran2fuc(cellList.get(i + 34)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 35)))
                        companyInfoDTO.setAccountCountry(WDWUtil.ran2fuc(cellList.get(i + 35)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 36))){}
                        companyInfoDTO.setCurrency(WDWUtil.ran2fuc(cellList.get(i + 36)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 37)))
                        companyInfoDTO.setBankPCD(WDWUtil.ran2fuc(cellList.get(i + 37)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 38)))
                        companyInfoDTO.setBankAddress(WDWUtil.ran2fuc(cellList.get(i + 38)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 39)))
                        companyInfoDTO.setSpecialName(WDWUtil.ran2fuc(cellList.get(i + 39)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 40)))
                        companyInfoDTO.setSpecialBank(WDWUtil.ran2fuc(cellList.get(i + 40)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 41)))
                        companyInfoDTO.setSpecialAccount(WDWUtil.ran2fuc(cellList.get(i + 41)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 42)))
                        companyInfoDTO.setSpecialSwiftCode(WDWUtil.ran2fuc(cellList.get(i + 42)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 43)))
                        companyInfoDTO.setFundsName(WDWUtil.ran2fuc(cellList.get(i + 43)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 44)))
                        companyInfoDTO.setFundsBank(WDWUtil.ran2fuc(cellList.get(i + 44)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 45)))
                        companyInfoDTO.setFundsAccount(WDWUtil.ran2fuc(cellList.get(i + 45)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 46)))
                        companyInfoDTO.setFundsSwiftCode(WDWUtil.ran2fuc(cellList.get(i + 46)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 47)))
                        companyInfoDTO.setSwiptAba(WDWUtil.ran2fuc(cellList.get(i + 47)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 48)))
                        companyInfoDTO.setOpeningAccountsPermitsUrl(WDWUtil.ran2fuc(cellList.get(i + 48)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 49)))
                        companyInfoDTO.setLegalName(WDWUtil.ran2fuc(cellList.get(i + 49)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 50))){
                        int identity = identitymap.get(WDWUtil.ran2fuc(cellList.get(i + 50)));
                        companyInfoDTO.setLegalIdentityType(identity);
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 51)))
                        companyInfoDTO.setLegalIdentityId(WDWUtil.ran2fuc(cellList.get(i + 51)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 52))){
                        // 证件到期日期
                        String identityDate = WDWUtil.ran2fuc(cellList.get(i + 52)).trim();
                        if(StringUtils.isNotEmpty(identityDate)){
                            identityDate = identityDate.substring(0,9);
                            identityDate = identityDate.replace("月","");
                            identityDate = identityDate.replace(" ","");
                            companyInfoDTO.setLegalIdentityDate(DateUtil.getCompanyDate(identityDate));
                        }
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 53)))
                        companyInfoDTO.setLegalCreAddress(WDWUtil.ran2fuc(cellList.get(i + 53)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 54)))
                        companyInfoDTO.setLegalCreDetailAddress(WDWUtil.ran2fuc(cellList.get(i + 54)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 55)))
                        companyInfoDTO.setLegalIdentityIUrl(WDWUtil.ran2fuc(cellList.get(i + 55)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 56)))
                        companyInfoDTO.setRealityName(WDWUtil.ran2fuc(cellList.get(i + 56)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 57))){
                        int identity = identitymap.get(WDWUtil.ran2fuc(cellList.get(i + 57)));
                        companyInfoDTO.setRealityIdentityType(identity);
                    }

                    if (StringUtils.isNotEmpty(cellList.get(i + 58)))
                        companyInfoDTO.setRealityIdentityId(WDWUtil.ran2fuc(cellList.get(i + 58)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 59))){
                        String identityDate = WDWUtil.ran2fuc(cellList.get(i + 59));
                        if(StringUtils.isNotEmpty(identityDate)){
                            identityDate = identityDate.substring(0,9);
                            identityDate = identityDate.replace("月","");
                            identityDate = identityDate.replace(" ","");
                            companyInfoDTO.setRealityIdentityDate(DateUtil.getCompanyDate(identityDate));
                        }
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 60)))
                        companyInfoDTO.setRealityCreAddress(WDWUtil.ran2fuc(cellList.get(i + 60)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 61)))
                        companyInfoDTO.setRealityCreDetailAddress(WDWUtil.ran2fuc(cellList.get(i + 61)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 62)))
                        companyInfoDTO.setRealityIdentityIUrl(WDWUtil.ran2fuc(cellList.get(i + 62)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 63)))
                        companyInfoDTO.setApplyName(WDWUtil.ran2fuc(cellList.get(i + 63)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 64))){
                        int identity = identitymap.get(WDWUtil.ran2fuc(cellList.get(i + 64)));
                        companyInfoDTO.setApplyIdentityType(identity);
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 65)))
                        companyInfoDTO.setApplyIdentityId(WDWUtil.ran2fuc(cellList.get(i + 65)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 66))){
                        // 证件到期日期
                        String identityDate = WDWUtil.ran2fuc(cellList.get(i + 66));
                        if(StringUtils.isNotEmpty(identityDate)){
                            identityDate = identityDate.substring(0,9);
                            identityDate = identityDate.replace("月","");
                            identityDate = identityDate.replace(" ","");
                            companyInfoDTO.setApplyIdentityDate(DateUtil.getCompanyDate(identityDate));
                        }
                    }
                    if (StringUtils.isNotEmpty(cellList.get(i + 67)))
                        companyInfoDTO.setApplyCreAddress(WDWUtil.ran2fuc(cellList.get(i + 67)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 68)))
                        companyInfoDTO.setApplyCreDetailAddress(WDWUtil.ran2fuc(cellList.get(i + 68)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 69)))
                        companyInfoDTO.setApplyIdentityIUrl(WDWUtil.ran2fuc(cellList.get(i + 69)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 70)))
                        companyInfoDTO.setContactName(WDWUtil.ran2fuc(cellList.get(i + 70)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 71)))
                        companyInfoDTO.setContactMobile(WDWUtil.ran2fuc(cellList.get(i + 71)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 72)))
                        companyInfoDTO.setContactEmail(WDWUtil.ran2fuc(cellList.get(i + 72)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 73)))
                        companyInfoDTO.setAdminName(WDWUtil.ran2fuc(cellList.get(i + 73)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 74)))
                        companyInfoDTO.setAdminMobile(WDWUtil.ran2fuc(cellList.get(i + 74)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 75)))
                        companyInfoDTO.setAdminEmail(WDWUtil.ran2fuc(cellList.get(i + 75)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 76)))
                        companyInfoDTO.setAdminQQ(WDWUtil.ran2fuc(cellList.get(i + 76)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 77)))
                        companyInfoDTO.setTechnicalName(WDWUtil.ran2fuc(cellList.get(i + 77)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 78)))
                        companyInfoDTO.setTechnicalMobile(WDWUtil.ran2fuc(cellList.get(i + 78)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 79)))
                        companyInfoDTO.setTechnicalEmail(WDWUtil.ran2fuc(cellList.get(i + 79)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 80)))
                        companyInfoDTO.setTechnicalQQ(WDWUtil.ran2fuc(cellList.get(i + 80)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 81)))
                        companyInfoDTO.setServiceName(WDWUtil.ran2fuc(cellList.get(i + 81)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 82)))
                        companyInfoDTO.setServiceQQ(WDWUtil.ran2fuc(cellList.get(i + 82)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 83)))
                        companyInfoDTO.setServiceTel(WDWUtil.ran2fuc(cellList.get(i + 83)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 84)))
                        companyInfoDTO.setWorkingTime(WDWUtil.ran2fuc(cellList.get(i + 84)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 85)))
                        companyInfoDTO.setMerchantNo(WDWUtil.ran2fuc(cellList.get(i + 85)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 86)))
                        companyInfoDTO.setMerchantBD(WDWUtil.ran2fuc(cellList.get(i + 86)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 87)))
                        companyInfoDTO.setMerchantBDEmail(WDWUtil.ran2fuc(cellList.get(i + 87)));
                    if (StringUtils.isNotEmpty(cellList.get(i + 88)))
                        companyInfoDTO.setMerchantBDTel(WDWUtil.ran2fuc(cellList.get(i + 88)));
                    companyInfoDTOS.add(companyInfoDTO);
                }
            }
        }
        return companyInfoDTOS;
    }


    /**
     * 读取Excel导入合同
     *
     * @param contractStringList
     * @return
     */
    public List<HistoryContractDTO> readExcelToContract(List<List<String>> contractStringList) {
        List<HistoryContractDTO> historyContractDTOS = new ArrayList<HistoryContractDTO>();
        Map<String, Integer> contractTypemap = getContractType();
        if (contractStringList != null) {
            //"第" + (i) + "行"
            for (int n = 1; n < contractStringList.size(); n++) {
                List<String> cellList = contractStringList.get(n);
                //"第" + (i) + "列"
                HistoryContractDTO historyContractDTO = new HistoryContractDTO();
                int i = 0;
                if (StringUtils.isNotEmpty(cellList.get(i)))
                    historyContractDTO.setContractName(WDWUtil.ran2fuc(cellList.get(i)));
                if (StringUtils.isNotEmpty(cellList.get(i + 1))){
                    int contractType = contractTypemap.get(WDWUtil.ran2fuc(cellList.get(i + 1)));
                    historyContractDTO.setContType(contractType);
                }
                if (StringUtils.isNotEmpty(cellList.get(i + 2)))
                    historyContractDTO.setPartyType(WDWUtil.ran2fuc(cellList.get(i + 2)));
                if (StringUtils.isNotEmpty(cellList.get(i + 3)))
                    historyContractDTO.setComName1(WDWUtil.ran2fuc(cellList.get(i + 3)));
                if (StringUtils.isNotEmpty(cellList.get(i + 4)))
                    historyContractDTO.setComName2(WDWUtil.ran2fuc(cellList.get(i + 4)));
                if (StringUtils.isNotEmpty(cellList.get(i + 5)))
                    historyContractDTO.setMerchantNo(WDWUtil.ran2fuc(cellList.get(i + 5)));
                if (StringUtils.isNotEmpty(cellList.get(i + 6)))
                    historyContractDTO.setContractNo(WDWUtil.ran2fuc(cellList.get(i + 6)));
                if (StringUtils.isNotEmpty(cellList.get(i + 7)))
                    historyContractDTO.setSignedTime(WDWUtil.ran2fuc(cellList.get(i + 7)));
                if (StringUtils.isNotEmpty(cellList.get(i + 8)))
                    historyContractDTO.setSignedAt(WDWUtil.ran2fuc(cellList.get(i + 8)));
                if (StringUtils.isNotEmpty(cellList.get(i + 9)))
                    historyContractDTO.setParentComid(WDWUtil.ran2fuc(cellList.get(i + 9)));
                if (StringUtils.isNotEmpty(cellList.get(i + 10)))
                    historyContractDTO.setBdName(WDWUtil.ran2fuc(cellList.get(i + 10)));
                historyContractDTOS.add(historyContractDTO);
            }
        }
        return historyContractDTOS;
    }

    public Map<String, Integer> getBizmap() {
        Map<String, Integer> bizmap = new HashMap<>();
        bizmap.put(BizTypeEnum.NOMAL.getName(), BizTypeEnum.NOMAL.getValue());
        bizmap.put(BizTypeEnum.GROUP.getName(), BizTypeEnum.GROUP.getValue());
        bizmap.put(BizTypeEnum.PREPAID.getName(), BizTypeEnum.PREPAID.getValue());
        bizmap.put(BizTypeEnum.AGENT.getName(), BizTypeEnum.AGENT.getValue());
        bizmap.put(BizTypeEnum.CROSSBORDER.getName(), BizTypeEnum.CROSSBORDER.getValue());
        bizmap.put(BizTypeEnum.DEPOSITTUBE.getName(), BizTypeEnum.DEPOSITTUBE.getValue());
        bizmap.put(BizTypeEnum.BANK8.getName(), BizTypeEnum.BANK8.getValue());
        bizmap.put(BizTypeEnum.BANK9.getName(), BizTypeEnum.BANK9.getValue());
        return bizmap;
    }

    public Map<String, Integer> getPaidtalCy() {
        Map<String, Integer> bizmap = new HashMap<>();
        bizmap.put(CurrencyEnum.RMB.getName(), CurrencyEnum.RMB.getValue());
        bizmap.put(CurrencyEnum.DOLLAR.getName(), CurrencyEnum.DOLLAR.getValue());
        return bizmap;
    }

    public Map<String, Integer> getIdentity() {
        Map<String, Integer> bizmap = new HashMap<>();
        bizmap.put(IdentityEnum.IDENTITY.getName(), IdentityEnum.IDENTITY.getValue());
        bizmap.put(IdentityEnum.PASSPORT.getName(), IdentityEnum.PASSPORT.getValue());
        bizmap.put(IdentityEnum.LICENSEITY.getName(), IdentityEnum.LICENSEITY.getValue());
        bizmap.put(IdentityEnum.COMMONITY.getName(), IdentityEnum.COMMONITY.getValue());
        bizmap.put(IdentityEnum.OTHERS.getName(), IdentityEnum.OTHERS.getValue());
        return bizmap;
    }

    public Map<String, Integer> getContractType() {
        Map<String, Integer> bizmap = new HashMap<>();
        bizmap.put(ContractTypeEnum.PAY.getName(), ContractTypeEnum.PAY.getValue());
        bizmap.put(ContractTypeEnum.DEPOSITORY.getName(), ContractTypeEnum.DEPOSITORY.getValue());
        bizmap.put(ContractTypeEnum.CROSSBORDER.getName(), ContractTypeEnum.CROSSBORDER.getValue());
        bizmap.put(ContractTypeEnum.PREPAIDCARD.getName(), ContractTypeEnum.PREPAIDCARD.getValue());
        bizmap.put(ContractTypeEnum.CHANNELTYPE.getName(), ContractTypeEnum.CHANNELTYPE.getValue());
        bizmap.put(ContractTypeEnum.OTHER.getName(), ContractTypeEnum.OTHER.getValue());
        return bizmap;
    }
}