package com.org.ecom.service;

import com.org.ecom.dto.UserDto;
import com.org.ecom.entities.User;
import com.org.ecom.repository.UserRepository;
import com.org.ecom.utility.ObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ObjectConverter objectConverter;

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


    public UserDto updateUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (!user.getIsActive()) {
            System.out.println("NO CHANGES IN UPDATE...");
            return userDto;
        }
        userRepository.delete(user);
        return objectConverter.entityToDto(userRepository.save(objectConverter.dtoToEntity(userDto)));
    }
}
