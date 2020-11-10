package com.softserveinc.ita.homeproject.config;

import java.util.HashSet;

import com.softserveinc.ita.homeproject.model.Permission;
import com.softserveinc.ita.homeproject.model.Role;
import com.softserveinc.ita.homeproject.model.User;
import com.softserveinc.ita.homeproject.repository.PermissionRepository;
import com.softserveinc.ita.homeproject.repository.RoleRepository;
import com.softserveinc.ita.homeproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author <a href="mailto:okhome@softserveinc.com">Oleksandr Khomenko</a>
 * <br>
 */
@Component
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
        user1("READ_NEWS_PERMISSION", "USER", "user@example.com");
        user1("DELETE_NEWS_PERMISSION", "ADMIN", "admin@example.com");
    }

    private void user1(String read_news_permission, String admin1, String email) {
        Permission perm2 = Permission.builder().name(read_news_permission).build();
        permissionRepository.save(perm2);
        Role admin = Role.builder().name(admin1).permissions(new HashSet<>() {{
            add(perm2);
        }})
                .build();
        roleRepository.save(admin);
        userRepository.save(User.builder()
                .firstName("User")
                .lastName("User")
                .email(email)
                .password(passwordEncoder.encode("password"))
                .roles(new HashSet<>() {{add(admin);}})
                .build());
    }
}
