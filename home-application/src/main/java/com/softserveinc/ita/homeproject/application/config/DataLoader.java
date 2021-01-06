package com.softserveinc.ita.homeproject.application.config;

import com.softserveinc.ita.homeproject.homedata.entity.Permission;
import com.softserveinc.ita.homeproject.homedata.entity.Role;
import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.repository.PermissionRepository;
import com.softserveinc.ita.homeproject.homedata.repository.RoleRepository;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DataLoader is the class that is used to upload the data into database.
 * To use it, please activate Spring profile - dev.
 *
 * @author Mykyta Morar
 * @author Ihor Svyrydenko
 *
 * ToDO Delete this class after configuring liquibase
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

    @Override
    public void run(ApplicationArguments args) {
        saveUser( "USER", "user@example.com", "GET_NEWS_PERMISSION" ,"GET_USER_PERMISSION");
        saveUser( "ADMIN", "admin@example.com",
                "CREATE_USER_PERMISSION", "UPDATE_USER_PERMISSION", "GET_USERS_PERMISSION", "GET_USER_PERMISSION", "DELETE_USER_PERMISSION",
                "UPDATE_NEWS_PERMISSION", "GET_NEWS_PERMISSION", "CREATE_NEWS_PERMISSION", "DELETE_NEWS_PERMISSION");
    }

    /**
     * Saves user with incoming parameters in the database.
     *
     * @param roleName the name of role
     * @param email email of the user
     * @param permissions permissions that are given to the user
     */
    private void saveUser(String roleName, String email, String... permissions) {
        List<Permission> perms = Arrays.stream(permissions)
                .map(permission -> Permission.builder().name(permission).build())
                .collect(Collectors.toList());
        permissionRepository.saveAll(perms);
        Role role = Role.builder().name(roleName).permissions(perms)
                .build();
        roleRepository.save(role);
        userRepository.save(User.builder()
                .firstName("User")
                .lastName("User")
                .email(email)
                .password(passwordEncoder.encode("password"))
                .expired(false)
                .locked(false)
                .credentialsExpired(false)
                .enabled(true)
                .roles(new HashSet<>() {{add(role);}})
                .build());
    }

}
