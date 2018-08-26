package com.keywordstone.contractmanagement.contract.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 10:09 2018/6/4
 * @Modified By:
 */
@Data
@Component
@ConfigurationProperties(prefix = "boss.restful")
public class RestfulConfig {

    //查询高级产品
    private String queryProduct;
    //查询高级产品开通状态
    private String queryOpenStatus;


}
