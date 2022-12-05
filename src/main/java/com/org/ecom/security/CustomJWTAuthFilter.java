package com.org.ecom.security;


import com.org.ecom.entity.registration.UserEntity;
import com.org.ecom.service.AccessTokensService;
import com.org.ecom.service.CustomUserService;
import com.org.ecom.service.UserService;
import com.org.ecom.utility.CustomJwtUtility;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Getter
@Setter


@Service
public class CustomJWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private UserService userService;

    @Autowired
    AccessTokensService accessTokensService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestTokenHeader = request.getHeader("Authorization");
        String username = "";
        String jwtToken = null;


        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

            jwtToken = requestTokenHeader.substring(7);

            username = jwtUtility.extractUsername(jwtToken);
            UserEntity userEntity = userService.findByUsername(username).get();


            if (accessTokensService.accessTokens(jwtToken, userEntity.getUserId()).isPresent()) {

                try {
                    username = jwtUtility.extractUsername(jwtToken);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


                UserDetails userDetails = customUserService.loadUserByUsername(username);


                if (username != null &&
                        SecurityContextHolder.getContext().getAuthentication() == null) {

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } else {
                    System.out.println("Token is not validated");
                }
            } else {
                System.out.println("Token is not correct");
            }
        }

        filterChain.doFilter(request, response);
    }
}
