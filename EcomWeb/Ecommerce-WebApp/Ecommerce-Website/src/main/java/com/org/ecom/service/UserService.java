package com.org.ecom.service;

import com.org.ecom.entities.UserEntity;
import com.org.ecom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    public Boolean findByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.get().getInvalidAttemptCount() > 1) {
            userRepository.updateInvalidAttemptCount(username);
        } else {
            userEntity.get().setInvalidAttemptCount(0);
            userEntity.get().setIsLocked(true);
            userRepository.save(userEntity.get());
            return false;
        }
        return true;
    }
}
