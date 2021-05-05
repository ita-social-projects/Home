package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;

public interface UserService extends QueryableService<User, UserDto> {

    UserDto createUser(UserDto createUserDto);

    UserDto updateUser(Long id, UserDto updateUserDto);

    void deactivateUser(Long id);
}
