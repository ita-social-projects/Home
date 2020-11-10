package com.softserveinc.ita.homeproject.repository;

import com.softserveinc.ita.homeproject.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:okhome@softserveinc.com">Oleksandr Khomenko</a>
 * <br>
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
}
