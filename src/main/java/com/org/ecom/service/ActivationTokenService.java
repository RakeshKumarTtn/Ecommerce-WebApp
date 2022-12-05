package com.org.ecom.service;

import com.org.ecom.entity.security.ActivationToken;
import com.org.ecom.repository.security.ActivationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ActivationTokenService {

    @Autowired
    ActivationTokenRepo activationTokenRepo;

    public void saveData(ActivationToken activationToken){
        activationTokenRepo.save(activationToken);
    }


    public Optional<ActivationToken> findByToken(String token){
        return activationTokenRepo.findByToken(token);
    }

    @Transactional
    public void deleteToken(Long user_id,String jwtToken){
        activationTokenRepo.delete(user_id,jwtToken);
    }
}
