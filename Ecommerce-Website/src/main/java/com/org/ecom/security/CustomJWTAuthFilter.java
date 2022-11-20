package com.org.ecom.security;


import com.org.ecom.service.CustomUserService;
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
import java.util.Date;


@Getter
@Setter


@Service
public class CustomJWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private CustomJwtUtility jwtUtility;

    @Autowired
    private CustomUserService userService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestTokenHeader = request.getHeader("Authorization");
        String username = "";
        String jwtToken = null;


        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

            jwtToken = requestTokenHeader.substring(7);


            Date diff = new Date(jwtUtility.extractExpiration(jwtToken).getTime() - jwtUtility.extractIssuedAt(jwtToken).getTime());

            Date date = new Date(1000 * 60 * 20);

            if (diff.getTime() - date.getTime() < 0) {

            try {
                username = jwtUtility.extractUsername(jwtToken);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }


            UserDetails userDetails = this.userService.loadUserByUsername(username);


            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                System.out.println("Token is not validated");
            }
        }
            else{
                System.out.println("Please enter Access Token");
            }

        }
        filterChain.doFilter(request, response);
}

}