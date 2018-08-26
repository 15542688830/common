package com.keywordstone.contractmanagement.contract.api.boss;

import com.keywordstone.contractmanagement.contract.api.dto.ContObjectDTO;
import com.keywordstone.contractmanagement.usercenter.sdk.dto.CompanyInfoDTO;
import lombok.Data;

import java.util.List;

@Data
public class ResBossDTO {
    private BossCompanyInfoDTO companyInfoDTO;
    private List<String> merchartNoList;
    private List<ContObjectDTO> contObjectDTOList;
    private String token;
    private String userId;
}
