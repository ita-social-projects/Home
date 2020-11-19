package com.softserveinc.ita.homeproject.service;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;

public interface UserService extends GenericServiceInterface<User, CreateUser, UpdateUser> {
}
