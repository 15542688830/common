package com.keywordstone.contractmanagement.contract.api.boss;

import com.keywordstone.contractmanagement.contract.api.dto.ContObjectDTO;
import com.keywordstone.contractmanagement.contract.api.service.ContractObjectService;
import com.keywordstone.contractmanagement.contract.api.service.ContractService;
import com.keywordstone.contractmanagement.contract.api.utils.FileUtil;
import com.keywordstone.contractmanagement.usercenter.sdk.dto.CompanyInfoDTO;
import com.keywordstone.contractmanagement.usercenter.sdk.dto.CompanyMerchantDTO;
import com.keywordstone.contractmanagement.usercenter.sdk.dto.MFixedCodeDTO;
import com.keywordstone.contractmanagement.usercenter.sdk.service.CompanyInfoSdkService;
import com.keywordstone.contractmanagement.usercenter.sdk.service.CompanyMerchantSdkService;
import com.keywordstone.contractmanagement.usercenter.sdk.service.MFixedCodeSdkService;
import com.keywordstone.framework.common.basic.dto.ResultDTO;
import com.keywordstone.framework.common.basic.dto.ResultError;
import com.keywordstone.framework.common.basic.enums.DataFlagEnum;
import com.keywordstone.framework.common.basic.utils.TokenUtils;
import com.keywordstone.framework.common.cache.redis.client.Redis;
import com.keywordstone.framework.common.database.mybatis.entity.DContMainEntity;
import com.keywordstone.framework.common.database.mybatis.entity.DUcCompanyMerchant;
import com.keywordstone.framework.common.database.mybatis.entity.MFixedCode;
import com.keywordstone.framework.common.database.mybatis.entity.MFixedCodeExample;
import com.keywordstone.framework.common.database.mybatis.service.MFixedCodeService;
import com.keywordstone.framework.common.security.server.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 客商信息查询接口
 *
 * @author SL
 * @date 2018/05/14
 */
@Service
@Slf4j
public class ButtBossServiceImpl implements ButtBossService {
    @Autowired
    private ContractService contractServiceImpl;

    @Autowired
    private CompanyInfoSdkService companyInfoSdkService;

    @Autowired
    private CompanyMerchantSdkService companyMerchantSdkService;

    @Autowired
    private ContractObjectService contractObjectService;

    @Autowired
    private MFixedCodeSdkService mFixedCodeSdkService;

    @Override
    public ResultDTO selecctCompanyInfo(ReqBossDTO params) {
        log.info("Boss调用查询接口");
        if (StringUtils.isEmpty(params.getContractNo())) {
            return ResultDTO.failure();
        }
        ResultDTO<List<DContMainEntity>> rst = contractServiceImpl.selectContractForBoss(params);
        List<DContMainEntity> dContMainEntities = rst.getData();
        //返回Boss系统DTO
        ResBossDTO resBossDTO = new ResBossDTO();
        //替换对接BossDTO-bossCompanyInfoDTO
        BossCompanyInfoDTO bossCompanyInfoDTO = new BossCompanyInfoDTO();
        if (dContMainEntities.size() != 0) {
            if (StringUtils.isNotEmpty(dContMainEntities.get(0).getCompId())) {
                //客商ID查询客商信息
                String compId = dContMainEntities.get(0).getCompId();
                ResultDTO<CompanyInfoDTO> companyInfoDTOResultDTO = companyInfoSdkService.getCompUnitByCompId(compId);
                CompanyMerchantDTO companyMerchantDTO = new CompanyMerchantDTO();
                companyMerchantDTO.setCompId(compId);
                //查询1级tradeType
                if (companyInfoDTOResultDTO.getData() != null) {
                    log.info("客商信息返回结果："+ companyInfoDTOResultDTO.getData());
                    if (StringUtils.isNotEmpty(companyInfoDTOResultDTO.getData().getTradeType())) {
                        MFixedCodeDTO mFixedCodeDTO = new MFixedCodeDTO();
                        mFixedCodeDTO.setName2(companyInfoDTOResultDTO.getData().getTradeType());
                        ResultDTO<List<MFixedCode>> rstmix = mFixedCodeSdkService.getTradeTypeByCondition(mFixedCodeDTO);
                        if (rstmix.getData().get(0) != null) {
                            mFixedCodeDTO.setId(rstmix.getData().get(0).getParentId());
                            ResultDTO<MFixedCode> resultDTO = mFixedCodeSdkService.getTradeTypeById(mFixedCodeDTO);
                            String tradeTypeParent = resultDTO.getData().getName2();
                            companyInfoDTOResultDTO.getData().setTradeTypeParent(tradeTypeParent);
                        }
                    }
                    //替换对接BossDTO
                    BeanUtils.copyProperties(companyInfoDTOResultDTO.getData(), bossCompanyInfoDTO);
                    //商户内外部分类
                    if (companyInfoDTOResultDTO.getData().getComInsideType() != null) {
                        bossCompanyInfoDTO.setComInsideType(comInsideTypeCode(companyInfoDTOResultDTO.getData().getComInsideType()));
                    }
                    //替换法人证件类型
                    if(companyInfoDTOResultDTO.getData().getLegalIdentityType()!=null) {
                        bossCompanyInfoDTO.setLegalIdentityType(BossIdentityCode(companyInfoDTOResultDTO.getData().getLegalIdentityType()));
                    }
                    if(companyInfoDTOResultDTO.getData().getRealityIdentityType()!=null) {
                        bossCompanyInfoDTO.setRealityIdentityType(BossIdentityCode(companyInfoDTOResultDTO.getData().getRealityIdentityType()));
                    }else {
                        bossCompanyInfoDTO.setRealityIdentityType(BossIdentityEnum.IDENTITY.getName());
                    }
                    log.info("查询客商信息成功，替换结果：" + bossCompanyInfoDTO);
                }
                //查询该客商的商户号
                ResultDTO<List<DUcCompanyMerchant>> resultDTO = companyMerchantSdkService.getCompanyAll(companyMerchantDTO);
                List<String> merchantNoList = new ArrayList<>();
                for (DUcCompanyMerchant dUcCompanyMerchant : resultDTO.getData()) {
                    merchantNoList.add(dUcCompanyMerchant.getMerchantNo());
                }
                log.info("查询客商关联商户号成功：" + merchantNoList);
                //合同ID查询合同标的
                String countId = dContMainEntities.get(0).getId();
                ContObjectDTO contObjectDTO = new ContObjectDTO();
                contObjectDTO.setContId(countId);
                contObjectDTO.setBasicDataFlag(1);
                ResultDTO<List<ContObjectDTO>> contractSubjects = contractObjectService.selecctContractSubject(contObjectDTO);
                log.info("查询关联合同标的成功：" + contractSubjects.getData());
                //拼装返回BOSS DTO
                resBossDTO.setCompanyInfoDTO(bossCompanyInfoDTO);
                resBossDTO.setContObjectDTOList(contractSubjects.getData());
                resBossDTO.setMerchartNoList(merchantNoList);
                String bossToken = FileUtil.getUUID();
                resBossDTO.setToken(bossToken);
                Redis.put("bossToken", bossToken);
                resBossDTO.setUserId("00c88fdd853f4b0580273d07d271439b");
                return ResultDTO.success(resBossDTO);
            }
        }
        return ResultDTO.success("无此协议编号");
    }

    @Override
    public ResultDTO addCompanyMerchant(ReqBossDTO params) {
        log.info("Boss调用新增接口 ：" + params);
        log.info("Boss Token：" + TokenUtils.getToken());
        if (StringUtils.isEmpty(Redis.get("bossToken")) || StringUtils.isEmpty(TokenUtils.getToken())) {
            return ResultDTO.failure(ResultError.error("权限不足"));
        } else {
            String bossRedisToken = Redis.get("bossToken");
            String boosHeaderToken = TokenUtils.getToken();
            if (!bossRedisToken.equals(boosHeaderToken)) {
                return ResultDTO.failure(ResultError.error("权限不足"));
            }
        }
        log.info("权限通过");
        ResultDTO<List<DContMainEntity>> rst = contractServiceImpl.selectContractForBoss(params);
        List<DContMainEntity> dContMainEntities = rst.getData();
        if (dContMainEntities.size() != 0) {
            DContMainEntity dContMainEntity = dContMainEntities.get(0);
            CompanyMerchantDTO companyMerchantDTO = new CompanyMerchantDTO();
            companyMerchantDTO.setCompId(dContMainEntity.getCompId());
            companyMerchantDTO.setCountId(dContMainEntity.getId());
            if (StringUtils.isNotEmpty(params.getMerchantNo())) {
                companyMerchantDTO.setMerchantNo(params.getMerchantNo());
            } else {
                return ResultDTO.failure(ResultError.error("商户号为空"));
            }

            ResultDTO resultDTO = companyMerchantSdkService.addCompanyMerchant(companyMerchantDTO);
            if (resultDTO.isSuccess()) {
                return ResultDTO.success("插入成功");
            } else {
                return resultDTO;
            }
        }
        return ResultDTO.success("无此协议编号");
    }

    //商户内外部分类对接BossDTO转换
    public String comInsideTypeCode(int value) {
        String name = "";
        if (BossComInsideTypeEnum.INSIDE.getValue() == value) {
            name = BossComInsideTypeEnum.INSIDE.getName();
        } else if (BossComInsideTypeEnum.EXTERNAL.getValue() == value) {
            name = BossComInsideTypeEnum.EXTERNAL.getName();
        } else if (BossComInsideTypeEnum.SYSTEM.getValue() == value) {
            name = BossComInsideTypeEnum.SYSTEM.getName();
        }
        return name;
    }

    //商户内外部分类对接BossDTO转换
    public String BossIdentityCode(int value) {
        //默认为身份证
        String name = BossIdentityEnum.IDENTITY.getName();
        if (BossIdentityEnum.IDENTITY.getValue() == value) {
            name = BossIdentityEnum.IDENTITY.getName();
        } else if (BossIdentityEnum.PASSPORT.getValue() == value) {
            name = BossIdentityEnum.PASSPORT.getName();
        }
        return name;
    }
}
