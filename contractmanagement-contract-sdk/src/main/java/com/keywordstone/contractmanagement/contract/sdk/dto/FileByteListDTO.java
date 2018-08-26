package com.keywordstone.contractmanagement.contract.sdk.dto;

import lombok.Data;

import java.util.List;

/**
 * 文件返回list
 *
 * @author SL
 * @date 2018/03/28
 */
@Data
public class FileByteListDTO {
    private String contName;
    private List<FileByteDto> fileByteDtoList;
}
