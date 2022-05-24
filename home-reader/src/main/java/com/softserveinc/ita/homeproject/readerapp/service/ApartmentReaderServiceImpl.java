package com.softserveinc.ita.homeproject.readerapp.service;

import java.util.List;

import com.softserveinc.ita.homeproject.readerapp.models.ApartmentReader;
import com.softserveinc.ita.homeproject.readerapp.repositories.ApartmentReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApartmentReaderServiceImpl implements ApartmentReaderService {

    @Autowired
    ApartmentReaderRepository apartmentReaderRepository;

    @Override
    public List<ApartmentReader> findAll() {
        return apartmentReaderRepository.findAll();
    }

    @Override
    public ApartmentReader createApartmentReader(ApartmentReader apartmentReader) {
        return apartmentReaderRepository.save(apartmentReader);
    }
}
