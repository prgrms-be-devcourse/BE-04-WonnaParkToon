package com.wonnapark.wnpserver.global.config;

import com.wonnapark.wnpserver.domain.oauth.config.OauthProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {OauthProperties.class})
public class PropertiesConfiguration {
}
