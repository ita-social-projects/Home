package com.softserveinc.ita.homeproject.readerapp.service;

import com.softserveinc.ita.homeproject.readerapp.models.ApartmentReader;
import com.softserveinc.ita.homeproject.readerapp.repositories.ApartmentReaderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ApartmentServiceImplTest {

    @Mock
    private ApartmentReaderRepository apartmentReaderRepository;

    @InjectMocks
    private ApartmentReaderServiceImpl apartmentReaderService;

    @Test
    void findApartments() {
        ApartmentReader apartment1 = ApartmentReader.builder()
                .apartmentNumber("26")
                .enabled(true)
                .build();

        ApartmentReader apartment2 = ApartmentReader.builder()
                .apartmentNumber("26")
                .enabled(true)
                .build();

        List<ApartmentReader> expected = new ArrayList<>();
        expected.add(apartment1);
        expected.add(apartment2);

        when(apartmentReaderRepository.findAll()).thenReturn(List.of(apartment1, apartment2));
        List<ApartmentReader> actual = apartmentReaderService.findAll();

        assertEquals(expected, actual);
        verify(apartmentReaderRepository, times(1)).findAll();
        verifyNoMoreInteractions(apartmentReaderRepository);
    }

    @Test
    void saveApartment() {
        ApartmentReader apartment = ApartmentReader.builder()
                .apartmentNumber("26")
                .enabled(true)
                .build();
        ApartmentReader apartmentCreated = ApartmentReader.builder()
                .apartmentNumber("26")
                .enabled(true)
                .build();
        ApartmentReader apartmentExpected = ApartmentReader.builder()
                .apartmentNumber("26")
                .enabled(true)
                .build();

        when(apartmentReaderRepository.save(apartment)).thenReturn(apartmentCreated);

        ApartmentReader apartmentActual = apartmentReaderService.createApartmentReader(apartment);
        assertEquals(apartmentExpected, apartmentActual);

        verify(apartmentReaderRepository, times(1)).save(apartment);
        verifyNoMoreInteractions(apartmentReaderRepository);
    }
}
