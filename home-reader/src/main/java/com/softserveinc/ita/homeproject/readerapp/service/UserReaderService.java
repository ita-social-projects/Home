package com.softserveinc.ita.homeproject.readerapp.service;

import com.softserveinc.ita.homeproject.readerapp.models.ReaderUser;

import java.util.List;

public interface UserReaderService {

    List<ReaderUser> findAll();
    void saveUser (ReaderUser userForMongo);
}
