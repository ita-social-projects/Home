package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperationRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserCredentialsRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homedata.user.role.RoleRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.UserDto;
import com.softserveinc.ita.homeproject.homeservice.exception.PasswordException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.user.UserServiceImpl;
import com.softserveinc.ita.homeproject.homeservice.util.ValidationHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserCooperationRepository userCooperationRepository;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private InvitationService<ApartmentInvitation, ApartmentInvitationDto> apartmentInvitationService;

    @Mock
    private InvitationService<CooperationInvitation, CooperationInvitationDto> cooperationInvitationService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCredentialsRepository userCredentialsRepository;

    @Mock
    private ServiceMapper mapper;

    private UserServiceImpl userService;

    @BeforeEach
    public void init() {
        ValidationHelper validationHelper = new ValidationHelper();
        userService = new UserServiceImpl(
                userRepository,
                roleRepository,
                userCooperationRepository,
                invitationRepository,
                apartmentInvitationService,
                cooperationInvitationService,
                passwordEncoder,
                userCredentialsRepository,
                mapper,
                validationHelper
        );
    }

    @Test
    void testCreateUserWithInvalidPassword() {
        UserDto userDto = new UserDto();
        userDto.setPassword("Ab$1234");
        userDto.setEmail("test@gmail.com");

        assertThrows(
                PasswordException.class,
                () -> userService.createUser(userDto)
        );
    }

    @Test
    void testCreateUserWithValidPassword() {
        UserDto userDto = new UserDto();
        userDto.setPassword("Ab1$1234");
        userDto.setEmail("test@gmail.com");

        assertThrows(
                NullPointerException.class,
                () -> userService.createUser(userDto)
        );
    }
}
