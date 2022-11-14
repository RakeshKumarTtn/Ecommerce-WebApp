package com.org.ecom.utility;

import com.org.ecom.constant.APPConstant;
import com.org.ecom.dto.UserDto;
import com.org.ecom.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectConverter {

    @Autowired
    ModelMapper modelMapper;

    public User dtoToEntity(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setIsActive(!APPConstant.IS_ACTIVE);
        user.setIsExpired(APPConstant.IS_EXPIRED);
        user.setIsDeleted(APPConstant.IS_DELETED);
        user.setIsLocked(APPConstant.IS_LOCKED);
        user.setInvalidAttemptCount(APPConstant.INVALID_ATTEMPT_COUNT);
        return user;
    }

    public UserDto entityToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setIsActive(user.getIsActive());
        userDto.setIsExpired(user.getIsExpired());
        userDto.setIsDeleted(user.getIsDeleted());
        userDto.setIsLocked(user.getIsLocked());
        user.setInvalidAttemptCount(user.getInvalidAttemptCount());
        return userDto;
    }
}
