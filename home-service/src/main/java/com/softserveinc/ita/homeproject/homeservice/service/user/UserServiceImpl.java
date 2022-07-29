package com.softserveinc.ita.homeproject.homeservice.service.user;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.BAD_REQUEST_USER_EXISTS_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_CURRENT_USER_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_INVITATION_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_USER_FORMAT_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_MATCH_EMAILS_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_SUPPORTED_PROVIDED_TYPE_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.USER_CANT_BE_DELETED_MESSAGE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperation;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperationRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homedata.user.role.RoleEnum;
import com.softserveinc.ita.homeproject.homedata.user.role.RoleRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.UserDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.PasswordException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;
import com.softserveinc.ita.homeproject.homeservice.util.ValidationHelper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserCooperationRepository userCooperationRepository;

    private final InvitationRepository invitationRepository;

    private final InvitationService<ApartmentInvitation, ApartmentInvitationDto> apartmentInvitationService;

    private final InvitationService<CooperationInvitation, CooperationInvitationDto> cooperationInvitationService;

    private final PasswordEncoder passwordEncoder;

    private final ValidationHelper validationHelper;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserCooperationRepository userCooperationRepository,
                           InvitationRepository invitationRepository,
                           @Qualifier("apartmentInvitationServiceImpl")
                               InvitationService<ApartmentInvitation, ApartmentInvitationDto>
                               apartmentInvitationService,
                           @Qualifier("cooperationInvitationServiceImpl")
                               InvitationService<CooperationInvitation, CooperationInvitationDto>
                               cooperationInvitationService,
                           PasswordEncoder passwordEncoder,
                           ServiceMapper mapper,
                           ValidationHelper validationHelper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userCooperationRepository = userCooperationRepository;
        this.invitationRepository = invitationRepository;
        this.apartmentInvitationService = apartmentInvitationService;
        this.cooperationInvitationService = cooperationInvitationService;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.validationHelper = validationHelper;
    }

    private final ServiceMapper mapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto createUserDto) {

        if (userRepository.findByEmail(createUserDto.getEmail()).isEmpty()) {
            if(!validationHelper.validatePasswordComplexity(createUserDto.getPassword())) {
                throw new PasswordException("Password too weak");
            }

            User toCreate = mapper.convert(createUserDto, User.class);

            toCreate.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
            toCreate.setEnabled(true);
            toCreate.setExpired(false);
            toCreate.setLocked(false);
            toCreate.setCredentialsExpired(false);
            toCreate.setCreateDate(LocalDateTime.now());
            toCreate.getContacts().forEach(contact -> {
                contact.setUser(toCreate);
                contact.setEnabled(true);
            });

            userRepository.save(toCreate);
            checkAndSaveUserByInvitation(createUserDto);
            return mapper.convert(toCreate, UserDto.class);
        }
        throw new BadRequestHomeException(String.format(BAD_REQUEST_USER_EXISTS_MESSAGE, createUserDto.getEmail()));
    }

    private void checkAndSaveUserByInvitation(UserDto userDto) {

        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(userDto.getRegistrationToken())
            .filter(invitation1 -> invitation1.getStatus().equals(InvitationStatus.PROCESSING)
                && invitation1.getEnabled().equals(true))
            .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_INVITATION_MESSAGE));

        validateEmailsMatching(invitation.getEmail(), userDto.getEmail());

        switch (invitation.getType()) {
            case APARTMENT:
                apartmentInvitationService.acceptUserInvitation((ApartmentInvitation) invitation);
                break;
            case COOPERATION:
                cooperationInvitationService.acceptUserInvitation((CooperationInvitation) invitation);
                break;
            default:
                throw new UnsupportedOperationException(NOT_SUPPORTED_PROVIDED_TYPE_MESSAGE);
        }
    }

    private void validateEmailsMatching(String invitationEmail, String email) {
        if (!invitationEmail.equals(email)) {
            throw new BadRequestHomeException(NOT_MATCH_EMAILS_MESSAGE);
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto updateUserDto) {
        User fromDB = userRepository.findById(id).filter(User::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_USER_FORMAT_MESSAGE, id)));

        validateEmailUniques(fromDB, updateUserDto);

        if (updateUserDto.getFirstName() != null) {
            fromDB.setFirstName(updateUserDto.getFirstName());
        }

        if (updateUserDto.getMiddleName() != null) {
            fromDB.setMiddleName(updateUserDto.getMiddleName());
        }

        if (updateUserDto.getLastName() != null) {
            fromDB.setLastName(updateUserDto.getLastName());
        }


        fromDB.setUpdateDate(LocalDateTime.now());
        userRepository.save(fromDB);
        return mapper.convert(fromDB, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto getCurrentUser() {
        return getCurrentUserByUsername();
    }

    private UserDto getCurrentUserByUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        User user = userRepository.findByEmail(username)
            .filter(User::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(NOT_FOUND_CURRENT_USER_MESSAGE));
        user.setContacts(user.getContacts().stream()
            .filter(Contact::getEnabled).collect(Collectors.toList()));
        return mapper.convert(user, UserDto.class);
    }

    private void validateEmailUniques(User user, UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail()).filter(User::getEnabled)
            .ifPresent(userByEmail -> {
                if (!user.getId().equals(userByEmail.getId())) {
                    throw new BadRequestHomeException(String.format(BAD_REQUEST_USER_EXISTS_MESSAGE,
                        userDto.getEmail()));
                }
            });
    }

    @Override
    public Page<UserDto> findAll(Integer pageNumber, Integer pageSize, Specification<User> specification) {
        Page<User> users = userRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize));
        users.forEach(user -> user.setContacts(user.getContacts().stream()
            .filter(Contact::getEnabled).collect(Collectors.toList())));
        return users.map(user -> mapper.convert(user, UserDto.class));
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        User toDelete = userRepository.findById(id).filter(User::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_USER_FORMAT_MESSAGE, id)));

        List<UserCooperation> userCooperation = userCooperationRepository.findUserCooperationByUser(toDelete);

        userCooperation.stream()
            .map(UserCooperation::getRole).forEach(role -> {
                if (role.equals(roleRepository.findByName(RoleEnum.ADMIN.getName().toUpperCase()).orElseThrow())) {
                    throw new BadRequestHomeException(USER_CANT_BE_DELETED_MESSAGE);
                }
            });

        toDelete.setEnabled(false);
        toDelete.getContacts().forEach(contact -> contact.setEnabled(false));
        userRepository.save(toDelete);
    }
}
