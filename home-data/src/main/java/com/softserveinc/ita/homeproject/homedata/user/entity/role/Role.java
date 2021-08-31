package com.softserveinc.ita.homeproject.homedata.user.entity.role;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.user.entity.permission.Permission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
@SequenceGenerator(name = "sequence", sequenceName = "roles_sequence")
public class Role extends BaseEntity {

    @ManyToMany
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permission> permissions;

    @Column(name = "name")
    private String name;
}
