package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * UserRepository is the interface that is needed
 * for interaction with Users in the database.
 *
 * @author Mykyta Morar
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Method for finding users in database by email
     *
     * @param email is the email of the user that is being searched
     * @return an instance of Optional(User) class
     */
    Optional<User> findByEmail(String email);

    Page<User> findAllByEnabledTrue(Pageable var1);
}
