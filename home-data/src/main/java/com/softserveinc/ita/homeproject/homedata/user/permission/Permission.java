package com.softserveinc.ita.homeproject.homedata.user.permission;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.user.role.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "permissions")
@SequenceGenerator(name = "sequence", sequenceName = "permissions_sequence")
public class Permission extends BaseEntity {

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;

    @Column(name = "name")
    private String name;
}
