package com.org.ecom.service;

import com.org.ecom.repository.UserRepository;
import com.org.ecom.utility.ObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectConverter objectConverter;
/*
    public UserDto addUser(UserDto userDto) {
        User user = objectConverter.dtoToEntity(userDto);
        user = userRepository.save(user);
        return objectConverter.entityToDto(user);
    }

    public Set<User> getActiveUser() {
        List<User> users = userRepository.findAll();
        Set<User> userList = new HashSet<>();
        userList = userRepository.listOfActiveUser("Something", true);
        return userList;
    }

    @Transactional
    public User deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        userRepository.deleteByEmail(!user.getIsActive(), email);
        return user;
    }

    @Transactional
    public User deleteUserByUserName(String username) {
        User user = userRepository.findByUsername(username);
        if (user.getIsActive())
            userRepository.deleteUserByUserName(!user.getIsActive(), username);
        return user;
    }


    @Transactional
    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        userRepository.delete(user);
        return objectConverter.entityToDto(userRepository.save(objectConverter.dtoToEntity(userDto)));
    }*/

/*    @Transactional
    public ResponseEntity updateUserPatch(UserDto userDto) {
        Optional<User> userOptional = null;
        if (userDto.getEmail() != null) {
            userOptional = Optional.of(userRepository.findByEmail(userDto.getEmail()));
        } else {
            return ResponseEntity.ok().body("Please Provide Email Id...");
        }
        userOptional.orElseThrow((ResourceNotFoundException::new));
        User user = userOptional.get();

        userRepository.updateUser(user.getUsername(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getEmail(), user.getPassword(), user.getIsActive(), user.getIsDeleted(), user.getIsLocked(), user.getIsExpired(), user.getPasswordUpdateDate(), user.getInvalidAttemptCount());
        return ResponseEntity.ok().body("profile is updated successfully");
    }*/
}


