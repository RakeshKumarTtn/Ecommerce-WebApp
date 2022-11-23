package com.org.ecom.service;

import com.org.ecom.entities.BlackListedTokens;
import com.org.ecom.repository.BlackListedTokensRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlackListedTokensService {

    @Autowired
    private BlackListedTokensRepo blackListedTokensRepo;

    public void saveToken(BlackListedTokens blackListedTokens) {
        blackListedTokensRepo.save(blackListedTokens);
    }

    public boolean findBlackListedTokens(String token) {
        if (blackListedTokensRepo.findByToken(token).isPresent()) {
            return false;
        } else {
            return true;
        }
    }
}
