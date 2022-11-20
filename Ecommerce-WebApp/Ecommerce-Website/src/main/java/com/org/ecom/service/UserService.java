package com.org.ecom.service;

import com.org.ecom.entities.Token;
import com.org.ecom.repository.TokenRepository;
import com.org.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public void saveUUIDTokenWithEmail(String email, String token) {
        Token userAccessToken =  new Token();
        userAccessToken.setToken(token);
        userAccessToken.setEmail(email);
        tokenRepository.save(userAccessToken);
    }
}


