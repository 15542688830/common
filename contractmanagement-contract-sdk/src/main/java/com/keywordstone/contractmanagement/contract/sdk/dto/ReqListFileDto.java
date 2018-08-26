package com.keywordstone.contractmanagement.contract.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

import java.util.List;

/**
 * 文件返回dto
 *
 * @author SL
 * @date 2018/03/1
 */
@Data
public class ReqListFileDto extends AbstractDTO {

    private List<ReqFileDto> reqfilelist;
}
