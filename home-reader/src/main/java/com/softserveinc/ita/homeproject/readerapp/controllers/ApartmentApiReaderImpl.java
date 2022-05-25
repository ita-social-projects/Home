package com.softserveinc.ita.homeproject.readerapp.controllers;

import java.util.List;

import com.softserveinc.ita.homeproject.readerapp.models.ApartmentReader;
import com.softserveinc.ita.homeproject.readerapp.service.ApartmentReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApartmentApiReaderImpl {

    @Autowired
    ApartmentReaderService apartmentReaderService;

    @GetMapping("/apartments")
    public List<ApartmentReader> getUsers() {
        return apartmentReaderService.findAll();
    }

    @PostMapping("/apartments")
    public void saveUsers(@RequestBody ApartmentReader apartmentReader) {
        apartmentReaderService.createApartmentReader(apartmentReader);
    }

}
