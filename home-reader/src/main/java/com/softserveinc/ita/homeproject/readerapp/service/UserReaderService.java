package com.softserveinc.ita.homeproject.readerapp.service;

import java.util.List;

import com.softserveinc.ita.homeproject.readerapp.models.UserReader;

public interface UserReaderService {

    List<UserReader> findAll();

    UserReader saveUser(UserReader user);
}
