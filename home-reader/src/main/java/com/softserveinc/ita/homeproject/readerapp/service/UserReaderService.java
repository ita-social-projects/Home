package com.softserveinc.ita.homeproject.readerapp.service;

import java.util.List;

import com.softserveinc.ita.homeproject.readerapp.models.ReaderUser;

public interface UserReaderService {

    List<ReaderUser> findAll();

    void saveUser(ReaderUser user);
}
