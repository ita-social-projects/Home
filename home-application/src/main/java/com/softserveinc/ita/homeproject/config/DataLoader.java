package com.softserveinc.ita.homeproject.config;

import com.softserveinc.ita.homeproject.model.Permission;
import com.softserveinc.ita.homeproject.model.Role;
import com.softserveinc.ita.homeproject.model.User;
import com.softserveinc.ita.homeproject.repository.PermissionRepository;
import com.softserveinc.ita.homeproject.repository.RoleRepository;
import com.softserveinc.ita.homeproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="mailto:okhome@softserveinc.com">Oleksandr Khomenko</a>
 * <br>
 */
@Component
@Profile("dev")
public class DataLoader implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataLoader(UserRepository userRepository, RoleRepository roleRepository, PermissionRepository permissionRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void run(ApplicationArguments args) {
        user1( "USER", "user@example.com", "READ_NEWS_PERMISSION");
        user1( "ADMIN", "admin@example.com", "UPDATE_NEWS_PERMISSION", "READ_NEWS_PERMISSION", "CREATE_NEWS_PERMISSION");
    }

    private void user1(String admin1, String email, String... read_news_permission) {
        Set<Permission> perm2 = Arrays.stream(read_news_permission).map(perm -> Permission.builder().name(perm).build()).collect(Collectors.toSet());
        permissionRepository.saveAll(perm2);
        Role admin = Role.builder().name(admin1).permissions(perm2)
                .build();
        roleRepository.save(admin);
        userRepository.save(User.builder()
                .firstName("User")
                .lastName("User")
                .email(email)
                .password(passwordEncoder.encode("password"))
                .expired(false)
                .locked(false)
                .credentialsExpired(false)
                .enabled(true)
                .roles(new HashSet<>() {{add(admin);}})
                .build());
    }
}
