package com.org.ecom.utility;

//import com.org.ecom.constant.APPConstant;

import com.org.ecom.dto.UserDto;
import com.org.ecom.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ObjectConverter {

    ModelMapper modelMapper = new ModelMapper();

    public User dtoToEntity(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        return user;
    }

    public UserDto entityToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }
}
