package com.org.ecom.service;

import com.org.ecom.dto.UserDto;
import com.org.ecom.entities.User;
import com.org.ecom.repository.UserRepository;
import com.org.ecom.utility.ObjectConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<com.org.ecom.entities.User> getActiveUser() {
        List<com.org.ecom.entities.User> users = userRepository.findAll();
        List<com.org.ecom.entities.User> userList = new ArrayList<>();

        for (com.org.ecom.entities.User user : users) {
            com.org.ecom.entities.User user1 = userRepository.listOfActiveUser(user.getEmail(), user.getIsActive());
            userList.add(user1);
        }
        return userList;
    }
}
