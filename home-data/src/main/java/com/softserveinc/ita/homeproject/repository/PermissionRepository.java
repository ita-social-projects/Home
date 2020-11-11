package com.softserveinc.ita.homeproject.repository;

import com.softserveinc.ita.homeproject.model.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Long> {
}
