package com.softserveinc.ita.homeproject.readerapp.controllers;

import java.util.List;

import com.softserveinc.ita.homeproject.readerapp.models.UserForMongo;
import com.softserveinc.ita.homeproject.readerapp.repositories.UserReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiReaderImpl {

    //    private UserReaderServise userReaderServise;
    @Autowired
    private UserReaderRepository userReaderRepository;

    @GetMapping("/users")
    public List<UserForMongo> getUsers() {
        return userReaderRepository.findAll();
    }


    @PostMapping("/users")
    public void saveUsers(@RequestBody UserForMongo userForMongo) {
        userReaderRepository.save(userForMongo);
    }
}
