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

    @Transactional
    public void expiredToken(Date date){
        refreshTokenRepo.deleteExpiredTokens(date);
    }

    public Optional<Set<RefreshTokens>> findById(Long user_id){
        return refreshTokenRepo.findByUserId(user_id);
    }

    public void saveData(RefreshTokens refreshTokens){
        refreshTokenRepo.save(refreshTokens);
    }

    public Optional<RefreshTokens> refreshTokens(String token,Long id){
        return refreshTokenRepo.findByToken(token, id);
    }
}
