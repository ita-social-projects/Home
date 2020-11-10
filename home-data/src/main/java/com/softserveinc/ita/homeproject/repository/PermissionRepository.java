package com.softserveinc.ita.homeproject.repository;

import com.softserveinc.ita.homeproject.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="mailto:okhome@softserveinc.com">Oleksandr Khomenko</a>
 * <br>
 */
@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
}
