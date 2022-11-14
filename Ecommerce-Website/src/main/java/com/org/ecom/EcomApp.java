package com.org.ecom;

import com.org.ecom.entities.User;
import com.org.ecom.security.SpringSecurityAuditorAware;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class EcomApp {

    @Bean
    public AuditorAware<User> auditorAware() {
        return AuditorAware;
    }

    public static void main(String[] args) {
        SpringApplication.run(EcomApp.class, args);
    }
}
