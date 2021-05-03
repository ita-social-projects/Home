package com.softserveinc.ita.homeproject.application.service.impl;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.config.HomeUserWrapperDetails;
import com.softserveinc.ita.homeproject.homedata.entity.Permission;
import com.softserveinc.ita.homeproject.homedata.entity.Role;
import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * HomeUserDetailsService class provides user information
 *
 * @author Ihor Svyrydenko
 */
@Component
public class HomeUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public HomeUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * loadUserByUsername method is used to load user from database
     *
     * @param email is user's email by which we are gonna search user
     * @return an UserDetails' instance
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("There is no user with given email!"));
        Set<Permission> perms = user.getRoles().stream()
            .map(Role::getPermissions)
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        return new HomeUserWrapperDetails(user, perms);
    }
}
