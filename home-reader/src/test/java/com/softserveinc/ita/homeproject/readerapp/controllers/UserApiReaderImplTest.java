package com.softserveinc.ita.homeproject.readerapp.controllers;

import com.softserveinc.ita.homeproject.readerapp.models.UserReader;
import com.softserveinc.ita.homeproject.readerapp.service.UserReaderServiceImpl;
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
class UserApiReaderImplTest {

    private MockMvc mockMvc;

    @InjectMocks
    UserApiReaderImpl controller;

    @Mock
    UserReaderServiceImpl userReaderService;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void getUserTest() throws Exception {

        UserReader user = UserReader.builder()
                .firstName("firstName")
                .lastName("lastName")
                .middleName("middleName")
                .build();

        when(userReaderService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("http://localhost:8099/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userReaderService, times(1)).findAll();
        verifyNoMoreInteractions(userReaderService);
    }
}
