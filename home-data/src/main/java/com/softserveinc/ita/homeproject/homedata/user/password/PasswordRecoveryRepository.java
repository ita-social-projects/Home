package com.softserveinc.ita.homeproject.homedata.user.password;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Long> {
}
