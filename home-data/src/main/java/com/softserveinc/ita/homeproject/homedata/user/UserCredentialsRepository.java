package com.softserveinc.ita.homeproject.homedata.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Long> {

    Optional<UserCredentials> findByEmail(String email);
}
