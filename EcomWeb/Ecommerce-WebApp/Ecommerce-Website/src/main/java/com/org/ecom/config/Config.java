package com.org.ecom.config;

import com.org.ecom.security.CustomJWTAuthFilter;
import com.org.ecom.service.CustomUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@ComponentScan("java")
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Config extends WebSecurityConfigurerAdapter {

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
                .antMatchers("/api/v1/register/**").permitAll()
                .antMatchers("/api/v1/seller/**").hasAnyAuthority("ADMIN","SELLER")
                .antMatchers("/api/v1/customer/**").hasAnyAuthority("ADMIN","CUSTOMER")
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
        //this will do nothing just return password as it is.
//        return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder(10);
    }

}
