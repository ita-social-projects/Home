package com.softserveinc.ita.homeproject.readerapp.controllers;

import com.softserveinc.ita.homeproject.readerapp.models.ApartmentReader;
import com.softserveinc.ita.homeproject.readerapp.service.ApartmentReaderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
class ApartmentApiReaderImplTest {

    private MockMvc mockMvc;

    @InjectMocks
    ApartmentApiReaderImpl controller;

    @Mock
    ApartmentReaderServiceImpl apartmentReaderService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void getApartmentTest() throws Exception {

        ApartmentReader apartment = ApartmentReader.builder()
                .apartmentNumber("26")
                .enabled(true)
                .build();

        when(apartmentReaderService.findAll()).thenReturn(List.of(apartment));
        mockMvc.perform(get("http://localhost:8099/apartments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(apartmentReaderService, times(1)).findAll();
        verifyNoMoreInteractions(apartmentReaderService);
    }
}
