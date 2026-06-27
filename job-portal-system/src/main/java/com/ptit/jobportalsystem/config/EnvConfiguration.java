package com.ptit.jobportalsystem.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfiguration {
    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    public Dotenv dotenv() {

        String envFile = ".env." + activeProfile;

        return Dotenv.configure()
                .directory("./")
                .filename(envFile)
                .ignoreIfMalformed()
                .ignoreIfMissing()
                .load();
    }

}
