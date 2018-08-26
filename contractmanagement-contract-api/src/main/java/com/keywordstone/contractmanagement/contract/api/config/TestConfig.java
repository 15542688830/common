package com.keywordstone.contractmanagement.contract.api.config;

import com.keywordstone.contractmanagement.contract.api.module.TestModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Configuration
public class TestConfig {

    @Value("${spring.application.name}")
    private String appName;
    @Value("${server.port}")
    private String serverPort;

    @Bean
    protected TestModule module() {
        log.info(appName);
        log.info(serverPort);
        TestModule module = new TestModule();
        module.setMessage("module!!!");
        return module;
    }

}
