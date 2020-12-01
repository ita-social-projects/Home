package com.softserveinc.ita.homeproject.service;

import com.softserveinc.ita.homeproject.service.dto.UserDto;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDto createUser(UserDto createUserDto);

    UserDto updateUser(Long id, UserDto updateUserDto);

    Page<UserDto> getAllUsers(Integer pageNumber, Integer pageSize);

    UserDto getUserById(Long id);

    void deactivateUser(Long id);
}
