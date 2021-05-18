package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * RoleRepository is the interface that is needed
 * for interaction with Roles in the database.
 *
 * @author Mykyta Morar
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    /**
     * Method for finding roles in database by name
     *
     * @param name is the name of the role
     * @return an instance of Role class
     */
    Optional<Role> findByName(String name);
}
