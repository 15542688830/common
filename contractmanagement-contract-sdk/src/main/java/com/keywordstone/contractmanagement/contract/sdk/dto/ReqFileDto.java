package com.keywordstone.contractmanagement.contract.sdk.dto;

import com.keywordstone.framework.common.basic.dto.AbstractDTO;
import lombok.Data;

import java.util.Date;


/**
 * 文件入口访问dto
 *
 * @author SL
 * @date 2018/01/18
 */
@Data
public class ReqFileDto extends AbstractDTO {
    /**
     * 访问需参数
     */
    private String fileId;
    private String fileType;
    private Integer type;
    private Integer archived;
    private String archivedName;
    private Integer status;
    private String countId;
    private String companyId;
    private int countType;
    private String fileStatus;
    private String uploadUser;
    private int pageSize;
    private int pageNumber;
    private Integer dataFlag;

    /**
     * 返回参数
     * id  文件id
     */
    private String id;
    private String fileDespId;
    private String name;
    private String fileNo;
    private String filePath;
    private String fileHttpPath;
    private String fileName;
    private String desp;
    private String countName;
    private Integer verId;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String updateUser;
}
