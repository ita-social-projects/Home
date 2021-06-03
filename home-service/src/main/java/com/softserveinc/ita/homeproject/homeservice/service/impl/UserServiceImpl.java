package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static com.softserveinc.ita.homeproject.homeservice.constants.Roles.ADMIN_ROLE;
import static com.softserveinc.ita.homeproject.homeservice.constants.Roles.USER_ROLE;

import java.time.LocalDateTime;
import java.util.Set;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.repository.RoleRepository;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.homeservice.exception.AlreadyExistHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.UserService;
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

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

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
            toCreate.setRoles(Set.of(roleRepository.findByName(USER_ROLE)
                    .orElseThrow(() -> new NotFoundHomeException("RoleDto not found."))));
            toCreate.setCreateDate(LocalDateTime.now());
            toCreate.getContacts().forEach(contact -> {
                contact.setUser(toCreate);
                contact.setEnabled(true);
            });

            userRepository.save(toCreate);

            return mapper.convert(toCreate, UserDto.class);
        }
        throw new AlreadyExistHomeException("User with email " + createUserDto.getEmail() + " is already exists");
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
                    throw new AlreadyExistHomeException("User with email " + userDto.getEmail() + " is already exists");
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

        toDelete.getRoles().forEach(
                role -> {
                    if(role.equals(roleRepository.findByName(ADMIN_ROLE).orElseThrow())) {
                        throw new BadRequestHomeException("User cannot be deleted.");
                    }
                }
        );

        toDelete.setEnabled(false);
        toDelete.getContacts().forEach(contact -> contact.setEnabled(false));
        userRepository.save(toDelete);
    }

}
