package com.softserveinc.ita.homeproject.readerapp.service;

import java.util.List;

import com.softserveinc.ita.homeproject.readerapp.models.ApartmentReader;

public interface ApartmentReaderService {

    List<ApartmentReader> findAll();

    ApartmentReader createApartmentReader(ApartmentReader apartmentReader);
}
