package com.org.ecom.service;

import com.org.ecom.entity.security.AccessTokens;
import com.org.ecom.repository.security.AccessTokensRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class AccessTokensService {

    @Autowired
    AccessTokensRepo accessTokensRepo;

    @Transactional
    public void logout(String token){
        accessTokensRepo.logoutDeleteToken(token);
    }

    @Transactional
    public void expiredToken(Date date){
        accessTokensRepo.deleteExpiredTokens(date);
    }

    public Optional<Set<AccessTokens>> findById(Long user_id){
    return accessTokensRepo.findByUserId(user_id);
    }

    public void saveData(AccessTokens accessTokens){
        accessTokensRepo.save(accessTokens);
    }

    public Optional<AccessTokens> accessTokens(String token, Long id){
        return accessTokensRepo.findByToken(token, id);
    }
}
