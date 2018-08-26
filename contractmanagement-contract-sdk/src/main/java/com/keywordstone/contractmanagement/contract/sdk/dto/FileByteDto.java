package com.keywordstone.contractmanagement.contract.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

@Data
public class FileByteDto extends AbstractDTO {
   private byte[] buff;
   private String fileName;
   private String fileId;
}
