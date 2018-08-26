package com.keywordstone.contractmanagement.contract.api.dto;

import lombok.Data;

/**
 * 乙方
 *
 * @author SL
 * @date 2018-04-17
 */
@Data
public class ContractSecondPartyDTO extends ContractPartyDTO {
    private String id;
    private String secondPartyNo;
}
