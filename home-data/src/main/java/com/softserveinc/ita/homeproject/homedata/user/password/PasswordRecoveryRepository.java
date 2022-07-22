package com.softserveinc.ita.homeproject.homedata.user.password;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long> {

    List<PasswordRecovery> findAllByEmail(String email);

    Optional<PasswordRecovery> findByRecoveryTokenAndEmail(String token, String email);
}
