package com.softserveinc.ita.homeproject.readerapp.service;

import java.util.List;

import com.softserveinc.ita.homeproject.readerapp.models.ReaderUser;
import com.softserveinc.ita.homeproject.readerapp.repositories.UserReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserReaderServiceImpl implements UserReaderService{

    @Autowired
    UserReaderRepository userReaderRepository;

    public List<ReaderUser> findAll() {
        return userReaderRepository.findAll();
    }

    public void saveUser(ReaderUser user) {
        userReaderRepository.save(user);
    }
}
