package com.org.ecom.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Setter
@Getter
public class CustomizedAuditorAware implements AuditorAware<String> {

    private String name;

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(name);
    }
}
