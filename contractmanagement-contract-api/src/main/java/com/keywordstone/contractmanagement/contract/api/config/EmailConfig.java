package com.keywordstone.contractmanagement.contract.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: ZhangRui
 * @Description:
 * @date: Created in 11:20 2018/5/11
 * @Modified By:
 */

@Data
@Component
@ConfigurationProperties(prefix = "email")
public class EmailConfig {

    /**
     * 服务器地址
     */
    private String server;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮件发送人名称
     */
    private String fromname;

    EmailConfig() {
    }
}