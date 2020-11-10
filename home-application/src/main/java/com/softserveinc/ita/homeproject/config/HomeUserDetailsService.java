package com.softserveinc.ita.homeproject.config;

import com.softserveinc.ita.homeproject.model.User;
import com.softserveinc.ita.homeproject.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class HomeUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;

    public HomeUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow();
        return new HomeUserPrincipal(user);
    }
}
