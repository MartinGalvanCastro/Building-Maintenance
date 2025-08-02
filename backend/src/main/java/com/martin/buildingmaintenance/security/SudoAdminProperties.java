package com.martin.buildingmaintenance.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "security.sudo")
@Data
public class SudoAdminProperties {

    private String username;
    private String password;
}
