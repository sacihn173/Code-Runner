package com.ContestSite.CodeCompiler.Configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "telegram")
public class TelegramConfig {

    private String token;

    private String username;

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return this.username;
    }

}
