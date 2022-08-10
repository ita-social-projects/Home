package com.softserveinc.ita.homeproject.homedata.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * UserCredentialRepository is the interface that is needed
 * for interaction with UserCredentials in the database.
 *
 * @author Yakiv Stoikov
 */

@Repository
public interface UserCredentialRepository extends PagingAndSortingRepository<UserCredentials, Long>,
    JpaSpecificationExecutor<UserCredentials> {

    /**
     * Method for finding user id in database by email and password
     *
     * @param email is the email of the user that is being searched
     * @param password is the password of the user that is being searched
     * @return an instance of Optional(Long) class
     */

    Optional<Long> findIdByEmailAndPassword(String email, String password);

    Optional<UserCredentials> findByEmail(String email);

}
