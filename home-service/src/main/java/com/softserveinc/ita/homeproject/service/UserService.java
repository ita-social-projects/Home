package com.softserveinc.ita.homeproject.service;

import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;

import java.util.Collection;

public interface UserService {

    ReadUser createUser(CreateUser createUserDto);

    ReadUser updateUser(Long id, UpdateUser updateUserDto);

    Collection<ReadUser> getAllUsers();

    ReadUser getUserById(Long id);

    void deactivateUser(Long id);
}
