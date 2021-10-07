package com.softserveinc.ita.homeproject.homeservice.service.user;

import static com.softserveinc.ita.homeproject.homeservice.constants.Roles.ADMIN_ROLE;

import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperation;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperationRepository;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homedata.user.role.RoleRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.apartment.ApartmentInvitationServiceImpl;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.cooperation.CooperationInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_FORMAT = "User with id: %d is not found";

    private static final String EMAILS_NOT_MATCH = "The e-mail to which the token was sent "
            + "does not match provided";

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserCooperationRepository userCooperationRepository;

    private final InvitationRepository invitationRepository;

    private final ApartmentInvitationServiceImpl apartmentInvitationService;

    private final CooperationInvitationService cooperationInvitationService;

    private final PasswordEncoder passwordEncoder;

    private final ServiceMapper mapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto createUserDto) {

        if (userRepository.findByEmail(createUserDto.getEmail()).isEmpty()) {
            User toCreate = mapper.convert(createUserDto, User.class);

            toCreate.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
            toCreate.setEnabled(true);
            toCreate.setExpired(false);
            toCreate.setCreateDate(LocalDateTime.now());
            toCreate.getContacts().forEach(contact -> {
                contact.setUser(toCreate);
                contact.setEnabled(true);
            });

            userRepository.save(toCreate);
            checkAndSaveUserByInvitation(createUserDto);
            return mapper.convert(toCreate, UserDto.class);
        }
        throw new BadRequestHomeException("User with email " + createUserDto.getEmail() + " is already exists");
    }

    private void checkAndSaveUserByInvitation(UserDto userDto) {

        var invitation = invitationRepository.findInvitationByRegistrationToken(userDto.getRegistrationToken())
                .filter(invitation1 -> invitation1.getStatus().equals(InvitationStatus.PROCESSING))
                .orElseThrow(() -> new NotFoundHomeException("Invitation with provided token not found"));

        validateEmailsMatching(invitation.getEmail(), userDto.getEmail());

        switch (invitation.getType()) {
            case APARTMENT:
                apartmentInvitationService.acceptUserInvitation(invitation);
                break;
            case COOPERATION:
                cooperationInvitationService.acceptUserInvitation(invitation);
                break;
            default:
                throw new UnsupportedOperationException("Provided type not supported");
        }
    }

    private void validateEmailsMatching(String invitationEmail, String email) {
        if (!invitationEmail.equals(email)) {
            throw new BadRequestHomeException(EMAILS_NOT_MATCH);
        }
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto updateUserDto) {
        User fromDB = userRepository.findById(id).filter(User::getEnabled)
                .orElseThrow(() -> new NotFoundHomeException(String.format(USER_NOT_FOUND_FORMAT, id)));

        validateEmailUniques(fromDB, updateUserDto);

        if (updateUserDto.getFirstName() != null) {
            fromDB.setFirstName(updateUserDto.getFirstName());
        }

        if (updateUserDto.getLastName() != null) {
            fromDB.setLastName(updateUserDto.getLastName());
        }

        if (updateUserDto.getEmail() != null) {
            fromDB.setEmail(updateUserDto.getEmail());
        }

        if (updateUserDto.getPassword() != null) {
            fromDB.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }

        fromDB.setUpdateDate(LocalDateTime.now());
        userRepository.save(fromDB);
        return mapper.convert(fromDB, UserDto.class);
    }

    private void validateEmailUniques(User user, UserDto userDto) {
        userRepository.findByEmail(userDto.getEmail()).filter(User::getEnabled)
                .ifPresent(userByEmail -> {
                    if (!user.getId().equals(userByEmail.getId())) {
                        throw new BadRequestHomeException("User with email "
                                + userDto.getEmail() + " is already exists");
                    }
                });
    }

    @Override
    public Page<UserDto> findAll(Integer pageNumber, Integer pageSize, Specification<User> specification) {
        return userRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
                .map(user -> mapper.convert(user, UserDto.class));
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        User toDelete = userRepository.findById(id).filter(User::getEnabled)
                .orElseThrow(() -> new NotFoundHomeException(String.format(USER_NOT_FOUND_FORMAT, id)));

        var userCooperation = userCooperationRepository.findUserCooperationByUser(toDelete);

        userCooperation.stream()
                .map(UserCooperation::getRole).forEach(role -> {
                    if (role.equals(roleRepository.findByName(ADMIN_ROLE).orElseThrow())) {
                        throw new BadRequestHomeException("User cannot be deleted");
                    }
                });

        toDelete.setEnabled(false);
        toDelete.getContacts().forEach(contact -> contact.setEnabled(false));
        userRepository.save(toDelete);
    }
}
