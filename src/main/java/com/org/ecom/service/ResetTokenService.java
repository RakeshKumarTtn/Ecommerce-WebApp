package com.org.ecom.service;

import com.org.ecom.entity.security.ResetToken;
import com.org.ecom.repository.security.ResetTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class ResetTokenService {

    @Autowired
    ResetTokenRepo resetTokenRepo;

    public void saveData(ResetToken resetToken){
        resetTokenRepo.save(resetToken);
    }

    public Optional<ResetToken> findByToken(String token){
        return resetTokenRepo.findByToken(token);
    }

    @Transactional
   public void deleteToken(Long user_id, String jwtToken){
        resetTokenRepo.delete(user_id,jwtToken);
   }
}
