package com.ContestSite.CodeCompiler.Configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "telegram")
public class TelegramConfig {

    private String token;

    private String username;

}
