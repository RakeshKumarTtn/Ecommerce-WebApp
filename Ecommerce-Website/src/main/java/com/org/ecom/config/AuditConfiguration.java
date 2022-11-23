package com.org.ecom.config;


import com.org.ecom.security.CustomizedAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfiguration {

    @Bean
    public CustomizedAuditorAware auditorProvider() {
        return new CustomizedAuditorAware();
    }
}
