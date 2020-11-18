package com.softserveinc.ita.homeproject.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import com.softserveinc.ita.homeproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;

    @SneakyThrows
    @Override
    public User create(CreateUser payload) {
        if (userRepository.existsByEmail(payload.getEmail())) {
            throw new Exception(); // here throw already exist exception
        } else {
            User toCreate = conversionService.convert(payload, User.class);
            return userRepository.save(toCreate);
        }
    }

    @Override
    public User update(Long id, UpdateUser payload) {
        User toUpdate = userRepository.findById(id).orElseThrow(); // here throw not found exception
        return userRepository.save(conversionService.convert(toUpdate, User.class));
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll().stream()
                .map(user -> conversionService.convert(user, User.class)) // here change returning class
                .collect(Collectors.toList());
    }

    @Override
    public User getById(Long id) {
        User toGet = userRepository.findById(id).orElseThrow(); // here throw not found exception
        return conversionService.convert(toGet, User.class); // here change returning class
    }

    @Override
    public boolean deleteById(Long id) {
        User toDelete = userRepository.findById(id).orElseThrow(); // here throw not found exception
        toDelete.setExpired(true);
        return true;
    }
}
