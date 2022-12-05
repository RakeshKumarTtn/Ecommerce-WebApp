package com.org.ecom.config;

import com.org.ecom.security.CustomJWTAuthFilter;
import com.org.ecom.security.CustomizedAuditorAware;
import com.org.ecom.service.CustomUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@ComponentScan("java")
@EnableWebSecurity
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class ProjectConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomJWTAuthFilter jwtAuthFilter;

    @Autowired
    private CustomUserService userService;


    // authenticate and authorize all requests using http basic.

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/user/**").permitAll()
                .antMatchers("/api/v1/user/upload_image").hasAnyAuthority("ADMIN", "SELLER", "CUSTOMER")
                .antMatchers("/api/v1/user/get_image").hasAnyAuthority("ADMIN", "SELLER", "CUSTOMER")
                .antMatchers("/api/v1/register/**").permitAll()
                .antMatchers("/api/v1/admin/**").hasAuthority("ADMIN")
                .antMatchers("/api/v1/admin/category/**").hasAnyAuthority("ADMIN")
                .antMatchers("/api/v1/product/home").permitAll()
                .antMatchers("/api/v1/product/admin/product/activate/{id}").hasAnyAuthority("ADMIN")
                .antMatchers("/api/v1/product/admin/product/deactivate/{id}").hasAnyAuthority("ADMIN")
                .antMatchers("/api/v1/product/admin/product/{productId}").hasAnyAuthority("ADMIN")
                .antMatchers("/api/v1/product/admin/allProducts").hasAnyAuthority("ADMIN")
                .antMatchers("/api/v1/product/**").hasAnyAuthority("SELLER")
                .antMatchers("/api/v1/seller/**").hasAnyAuthority("ADMIN", "SELLER")
                .antMatchers("/api/v1/customer/**").hasAnyAuthority("ADMIN", "CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // inMemoryAuthentication is used when we want to store multiple username and passwords along with roles.
    // validation is done until server iss running and when server shuts down memory is cleaned.

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userService);

    }

    // Bean is created for defining encoder for password necessary thing.

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public CustomizedAuditorAware auditorProvider() {
        return new CustomizedAuditorAware();
    }
}
