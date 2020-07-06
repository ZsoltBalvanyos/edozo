package com.edozo.java.test.configuration;

import io.micrometer.core.instrument.MeterRegistry;
import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConfigurationProperties
public class Metrics {

    @Value("${spring.application.name}")
    private String appName;

    @Value("${spring.application.env}")
    private String environment;

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags(
            "service", appName,
            "env", environment,
            "host", getHost());
    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost()
                .getHostName();
        } catch (UnknownHostException e) {
            log.warn("Unable to get machine hostname", e);
            return "Unknown Host";
        }
    }
}
