package com.org.ecom.service;

import com.org.ecom.entity.security.RefreshTokens;
import com.org.ecom.repository.security.RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepo refreshTokenRepo;

    //Method for expiring the token
    @Transactional
    public void expiredToken(Date date){
        refreshTokenRepo.deleteExpiredTokens(date);
    }

    //Method for fetching refresh token by id
    public Optional<Set<RefreshTokens>> findById(Long user_id){
        return refreshTokenRepo.findByUserId(user_id);
    }

    //Method for saving the refresh token in the Refresh Token table
    public void saveData(RefreshTokens refreshTokens){
        refreshTokenRepo.save(refreshTokens);
    }

    //Method for fetching the refresh token
    public Optional<RefreshTokens> refreshTokens(String token,Long id){
        return refreshTokenRepo.findByToken(token, id);
    }
}
