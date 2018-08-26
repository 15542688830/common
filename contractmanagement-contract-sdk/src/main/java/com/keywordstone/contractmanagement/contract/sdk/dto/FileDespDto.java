package com.keywordstone.contractmanagement.contract.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

@Data
public class FileDespDto extends AbstractDTO {
    private String id;
    private String fileId;
    private String countId;
    private String companyId;
    private String countFileNo;

}
