package com.softserveinc.ita.homeproject.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.repository.RoleRepository;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import com.softserveinc.ita.homeproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final RoleRepository roleRepository;

    @Transactional
    @Override
    public User create(CreateUser payload) {
        if (userRepository.findByEmail(payload.getEmail()).isPresent()) {
            throw new RuntimeException();  //TODO: here throw already exist exception
        } else {
            User toCreate = conversionService.convert(payload, User.class);
//          User toCreate = new User();
//          toCreate.setEmail(payload.getEmail());
//          toCreate.setPassword(payload.getPassword());
            toCreate.setEnabled(true);
            toCreate.setExpired(false);
            toCreate.setRoles(Set.of(roleRepository.findByName("USER")));
            return userRepository.save(toCreate);
        }
    }

    @Override
    public User update(Long id, UpdateUser payload) {
        if (userRepository.findById(id).isPresent()) {
            User toUpdate = conversionService.convert(payload, User.class);
            return userRepository.save(toUpdate);
        } else {
            throw new RuntimeException();  //TODO: here throw not found exception
        }
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll().stream()
               .map(user -> conversionService.convert(user, User.class))  //TODO: here change returning class
                .collect(Collectors.toList());
    }

    @Override
    public User getById(Long id) {
        User toGet = userRepository.findById(id)
                .orElseThrow();  //TODO: here throw not found exception
        return conversionService.convert(toGet, User.class);  //TODO: here change returning class
    }

    @Override
    public boolean deleteById(Long id) {
        User toDelete = userRepository.findById(id)
                .orElseThrow();  //TODO: here throw not found exception
        toDelete.setExpired(true);
        return true;
    }
}
