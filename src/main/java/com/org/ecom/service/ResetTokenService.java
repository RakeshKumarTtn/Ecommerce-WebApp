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

    /*
        Method for saving the token
    */
    public void saveData(ResetToken resetToken){
        resetTokenRepo.save(resetToken);
    }

    /*
        Method for fetching the token by id
    */
    public Optional<ResetToken> findByToken(String token){
        return resetTokenRepo.findByToken(token);
    }

    /*
        Method for deleting the token
    */
    @Transactional
   public void deleteToken(Long user_id, String jwtToken){
        resetTokenRepo.delete(user_id,jwtToken);
   }
}
