package com.hdw.mq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Descripton com.hdw.mq.config
 * @Author TuMinglong
 * @Date 2019/6/14 11:53
 */
@Component
@Data
@ConfigurationProperties(prefix = "hdw.activemq-jmx")
public class ActiveMQJmxConfig {

    private String connectorPort;

    private String connectorPath;

    private String jmxDomain;

    private String connectorIp;

    private String brokerName;

}
