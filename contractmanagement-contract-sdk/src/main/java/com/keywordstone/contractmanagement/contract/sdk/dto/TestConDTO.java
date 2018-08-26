package com.keywordstone.contractmanagement.contract.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class TestConDTO extends AbstractDTO {

    @NotEmpty(message = "不能为空")
    private String message;
}
