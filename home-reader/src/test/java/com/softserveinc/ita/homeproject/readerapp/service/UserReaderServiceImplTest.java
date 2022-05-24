package com.softserveinc.ita.homeproject.readerapp.service;

import com.softserveinc.ita.homeproject.readerapp.models.UserReader;
import com.softserveinc.ita.homeproject.readerapp.repositories.UserReaderRepository;
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
class UserReaderServiceImplTest {

    @Mock
    private UserReaderRepository userReaderRepository;

    @InjectMocks
    private UserReaderServiceImpl userReaderService;

    @Test
    void findUser() {

        UserReader user1 = UserReader.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .middleName("MiddleName")
                .build();

        UserReader user2 = UserReader.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .middleName("MiddleName")
                .build();

        List<UserReader> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);

        when(userReaderRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserReader> actual = userReaderService.findAll();

        assertEquals(expected, actual);
        verify(userReaderRepository, times(1)).findAll();
        verifyNoMoreInteractions(userReaderRepository);
    }

    @Test
    void createUser() {
        UserReader user = UserReader.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .middleName("MiddleName")
                .build();

        UserReader created = UserReader.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .middleName("MiddleName")
                .build();

        UserReader expectedUser = UserReader.builder()
                .firstName("FirstName")
                .lastName("LastName")
                .middleName("MiddleName")
                .build();

        when(userReaderRepository.save(user)).thenReturn(created);

        UserReader actual = userReaderService.saveUser(user);
        assertEquals(expectedUser, actual);
        verify(userReaderRepository, times(1)).save(user);
        verifyNoMoreInteractions(userReaderRepository);
    }
}
