package com.softserveinc.ita.homeproject.readerapp.controllers;

import java.util.List;

import com.softserveinc.ita.homeproject.readerapp.models.UserReader;
import com.softserveinc.ita.homeproject.readerapp.service.UserReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiReaderImpl {

    @Autowired
    private UserReaderService userReaderService;

    @GetMapping("/users")
    public List<UserReader> getUsers() {
        return userReaderService.findAll();
    }

    @PostMapping("/users")
    public void saveUsers(@RequestBody UserReader user) {
        userReaderService.saveUser(user);
    }
}
