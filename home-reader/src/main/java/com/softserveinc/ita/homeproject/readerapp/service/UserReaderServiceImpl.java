package com.softserveinc.ita.homeproject.readerapp.service;

import java.util.List;

import com.softserveinc.ita.homeproject.readerapp.models.UserReader;
import com.softserveinc.ita.homeproject.readerapp.repositories.UserReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserReaderServiceImpl implements UserReaderService{

    @Autowired
    UserReaderRepository userReaderRepository;

    public List<UserReader> findAll() {
        return userReaderRepository.findAll();
    }

    public UserReader saveUser(UserReader user) {
        return userReaderRepository.save(user);
    }
}
