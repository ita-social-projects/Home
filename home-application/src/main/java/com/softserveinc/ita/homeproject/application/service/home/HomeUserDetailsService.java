package com.softserveinc.ita.homeproject.application.service.home;

import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.config.HomeUserWrapperDetails;
import com.softserveinc.ita.homeproject.homedata.entity.permission.Permission;
import com.softserveinc.ita.homeproject.homedata.entity.user.User;
import com.softserveinc.ita.homeproject.homedata.repository.user.UserCooperationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.user.UserRepository;
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

    private final UserCooperationRepository userCooperationRepository;

    public HomeUserDetailsService(UserRepository userRepository, UserCooperationRepository userCooperationRepository) {
        this.userRepository = userRepository;
        this.userCooperationRepository = userCooperationRepository;
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
        var user = userRepository.findByEmail(email)
                .filter(User::getEnabled)
                .orElseThrow(() -> new UsernameNotFoundException("There is no user with given email!"));

        var userCooperation = userCooperationRepository.findUserCooperationByUser(user);


        List<String> permissions = userCooperation.stream()
                .map(userCooperation1 -> userCooperation1.getRole().getPermissions())
                .flatMap(List::stream)
                .collect(Collectors.toList())
                .stream()
                .map(Permission::getName)
                .collect(Collectors.toList());


        return new HomeUserWrapperDetails(user, permissions);
    }
}
