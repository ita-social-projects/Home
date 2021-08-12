package com.softserveinc.ita.homeproject.homedata.repository.permission;

import com.softserveinc.ita.homeproject.homedata.entity.permission.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * PermissionRepository is the interface that
 * is needed for interaction with Permissions
 * in the database.
 *
 * @author Mykyta Morar
 */
@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
}
