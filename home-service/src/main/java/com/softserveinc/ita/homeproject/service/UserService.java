package com.softserveinc.ita.homeproject.service;

import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;

import java.util.Collection;

public interface UserService {

    ReadUser createUser(CreateUser payload);

    ReadUser updateUser(Long id, UpdateUser payload);

    Collection<ReadUser> getAllUsers();

    ReadUser getUserByParameter(Long id);

    Long deactivateUser(Long id);
}
