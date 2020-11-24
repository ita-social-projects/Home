package com.softserveinc.ita.homeproject.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.repository.RoleRepository;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import com.softserveinc.ita.homeproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static com.softserveinc.ita.homeproject.application.constants.Roles.userRole;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public ReadUser createUser(CreateUser createUserDto) {
        if (userRepository.findByEmail(createUserDto.getEmail()).isPresent()) {
            throw new RuntimeException();  //TODO: here throw already exist exception
        } else {
            User toCreate = conversionService.convert(createUserDto, User.class);
            toCreate.setEnabled(true);
            toCreate.setExpired(false);
            toCreate.setRoles(Set.of(roleRepository.findByName(userRole)));

            userRepository.save(toCreate);

            return conversionService.convert(toCreate, ReadUser.class);
        }
    }

    @Override
    public ReadUser updateUser(Long id, UpdateUser updateUserDto) {
        if (userRepository.findById(id).isPresent()) {
            User toUpdate = conversionService.convert(updateUserDto, User.class);
            userRepository.save(toUpdate);
            return conversionService.convert(updateUserDto, ReadUser.class);
        } else {
            throw new RuntimeException();  //TODO: here throw not found exception
        }
    }

    @Override
    public Collection<ReadUser> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> conversionService.convert(user, ReadUser.class))
                .collect(Collectors.toList());
    }

    @Override
    public ReadUser getUserById(Long id) {
        User toGet = userRepository.findById(id)
                .orElseThrow();  //TODO: here throw not found exception
        return conversionService.convert(toGet, ReadUser.class);
    }

    @Override
    public void deactivateUser(Long id) {
        User toDelete = userRepository.findById(id)
                .orElseThrow();  //TODO: here throw not found exception
        toDelete.setEnabled(false);
    }
}
