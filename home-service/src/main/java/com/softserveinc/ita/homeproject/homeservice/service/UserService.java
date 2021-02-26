package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {

    UserDto createUser(UserDto createUserDto);

    UserDto updateUser(Long id, UserDto updateUserDto);

    Page<UserDto> findUsers(Integer pageNumber, Integer pageSize, Specification<User> specification);

    UserDto getUserById(Long id);

    void deactivateUser(Long id);
}
