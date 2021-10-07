package com.softserveinc.ita.homeproject.homeservice.service.user;

import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface UserService extends QueryableService<User, UserDto> {

    UserDto createUser(UserDto createUserDto);

    UserDto updateUser(Long id, UserDto updateUserDto);

    void deactivateUser(Long id);

}
