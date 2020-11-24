package com.softserveinc.ita.homeproject.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.repository.RoleRepository;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.service.UserService;
import com.softserveinc.ita.homeproject.service.dto.CreateUserDto;
import com.softserveinc.ita.homeproject.service.dto.ReadUserDto;
import com.softserveinc.ita.homeproject.service.dto.UpdateUserDto;
import com.softserveinc.ita.homeproject.service.exceptions.AlreadyExistException;
import com.softserveinc.ita.homeproject.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.softserveinc.ita.homeproject.service.constants.Roles.userRole;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final RoleRepository roleRepository;

    @SneakyThrows
    @Transactional
    @Override
    public ReadUserDto createUser(CreateUserDto createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new AlreadyExistException(409, "User with this email is already existing");
        } else {
            User toCreate = conversionService.convert(createUserDto, User.class);
            toCreate.setEnabled(true);
            toCreate.setExpired(false);
            toCreate.setRoles(Set.of(roleRepository.findByName(userRole)));

            userRepository.save(toCreate);

            return conversionService.convert(toCreate, ReadUserDto.class);
        }
    }

    @SneakyThrows
    @Override
    public ReadUserDto updateUser(Long id, UpdateUserDto updateUserDto) {
        if (userRepository.findById(id).isPresent()) {
            User toUpdate = conversionService.convert(updateUserDto, User.class);
            userRepository.save(toUpdate);
            return conversionService.convert(updateUserDto, ReadUserDto.class);
        } else {
            throw new NotFoundException(404, "User with this Id is not found");
        }
    }

    @Override
    public Collection<ReadUserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> conversionService.convert(user, ReadUserDto.class))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Override
    public ReadUserDto getUserById(Long id) {
        User toGet = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, "User with this Id is not found"));
        return conversionService.convert(toGet, ReadUserDto.class);
    }

    @SneakyThrows
    @Override
    public void deactivateUser(Long id) {
        User toDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(404, "User with this Id is not found"));
        toDelete.setEnabled(false);
    }
}
