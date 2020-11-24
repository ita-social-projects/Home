package com.softserveinc.ita.homeproject.service;

import com.softserveinc.ita.homeproject.service.dto.CreateUserDto;
import com.softserveinc.ita.homeproject.service.dto.ReadUserDto;
import com.softserveinc.ita.homeproject.service.dto.UpdateUserDto;

import java.util.Collection;

public interface UserService {

    ReadUserDto createUser(CreateUserDto createUserDto);

    ReadUserDto updateUser(Long id, UpdateUserDto updateUserDto);

    Collection<ReadUserDto> getAllUsers();

    ReadUserDto getUserById(Long id);

    void deactivateUser(Long id);
}
