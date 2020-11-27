package com.softserveinc.ita.homeproject.service;

import com.softserveinc.ita.homeproject.service.dto.CreateUserDto;
import com.softserveinc.ita.homeproject.service.dto.ReadUserDto;
import com.softserveinc.ita.homeproject.service.dto.UpdateUserDto;
import org.springframework.data.domain.Page;

public interface UserService {

    ReadUserDto createUser(CreateUserDto createUserDto);

    ReadUserDto updateUser(Long id, UpdateUserDto updateUserDto);

    Page<ReadUserDto> getAllUsers(Integer pageNumber, Integer pageSize);

    ReadUserDto getUserById(Long id);

    void deactivateUser(Long id);
}
