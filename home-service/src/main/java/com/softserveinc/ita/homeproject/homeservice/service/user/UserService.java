package com.softserveinc.ita.homeproject.homeservice.service.user;

import com.softserveinc.ita.homeproject.homedata.user.entity.User;
import com.softserveinc.ita.homeproject.homeservice.dto.user.UserDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface UserService extends QueryableService<User, UserDto> {

    UserDto createUser(UserDto createUserDto);

    UserDto updateUser(Long id, UserDto updateUserDto);

    void deactivateUser(Long id);

}
