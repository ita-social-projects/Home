package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperationRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserCredentialsRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserSession;
import com.softserveinc.ita.homeproject.homedata.user.UserSessionRepository;
import com.softserveinc.ita.homeproject.homedata.user.password.PasswordRecovery;
import com.softserveinc.ita.homeproject.homedata.user.password.PasswordRecoveryRepository;
import com.softserveinc.ita.homeproject.homedata.user.password.enums.PasswordRecoveryTokenStatus;
import com.softserveinc.ita.homeproject.homedata.user.role.RoleRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.UserDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.password.PasswordRestorationApprovalDto;
import com.softserveinc.ita.homeproject.homeservice.exception.PasswordException;
import com.softserveinc.ita.homeproject.homeservice.exception.PasswordRestorationException;
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

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSessionRepository userSessionRepository;

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
    private PasswordRecoveryRepository passwordRecoveryRepository;

    @Mock
    private ServiceMapper mapper;

    private UserServiceImpl userService;

    @BeforeEach
    public void init() {
        ValidationHelper validationHelper = new ValidationHelper();
        userService = new UserServiceImpl(
            userRepository,
            userSessionRepository,
            roleRepository,
            userCooperationRepository,
            invitationRepository,
            apartmentInvitationService,
            cooperationInvitationService,
            passwordEncoder,
            mapper,
            validationHelper,
            passwordRecoveryRepository,
            userCredentialsRepository
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

    @Test
    void testUpdatePasswordWithExpiredStatusToken() {
        PasswordRestorationApprovalDto approvalDto = new PasswordRestorationApprovalDto();
        PasswordRecovery recoveryToken = new PasswordRecovery();

        approvalDto.setEmail("email@mailbox.com");
        approvalDto.setToken("e550d31e-0dbe-11ed-8502-6547419ef7e6");
        approvalDto.setNewPassword("n3wPassword");
        approvalDto.setPasswordConfirmation("n3wPassword");
        recoveryToken.setEmail("email@mailbox.com");
        recoveryToken.setRecoveryToken("e550d31e-0dbe-11ed-8502-6547419ef7e6");
        recoveryToken.setSentDateTime(LocalDateTime.now().minusMinutes(1L));
        recoveryToken.setStatus(PasswordRecoveryTokenStatus.EXPIRED);
        recoveryToken.setEnabled(true);
        when(passwordRecoveryRepository.findByRecoveryTokenAndEmail(approvalDto.getEmail(),
            approvalDto.getToken())).thenReturn(Optional.of(recoveryToken));

        assertThrows(PasswordRestorationException.class,
            () -> userService.changePassword(approvalDto));
    }

    @Test
    void testUpdatePasswordWithExpiredToken() {
        PasswordRestorationApprovalDto approvalDto = new PasswordRestorationApprovalDto();
        PasswordRecovery recoveryToken = new PasswordRecovery();

        approvalDto.setEmail("email@mailbox.com");
        approvalDto.setToken("e550d31e-0dbe-11ed-8502-6547419ef7e6");
        approvalDto.setNewPassword("n3wPassword");
        approvalDto.setPasswordConfirmation("n3wPassword");
        recoveryToken.setEmail("email@mailbox.com");
        recoveryToken.setRecoveryToken("e550d31e-0dbe-11ed-8502-6547419ef7e6");
        recoveryToken.setSentDateTime(LocalDateTime.now().minusDays(1L));
        recoveryToken.setStatus(PasswordRecoveryTokenStatus.ACTIVE);
        recoveryToken.setEnabled(true);
        when(passwordRecoveryRepository.findByRecoveryTokenAndEmail(approvalDto.getEmail(),
            approvalDto.getToken())).thenReturn(Optional.of(recoveryToken));

        assertThrows(PasswordRestorationException.class,
            () -> userService.changePassword(approvalDto));
    }
}
