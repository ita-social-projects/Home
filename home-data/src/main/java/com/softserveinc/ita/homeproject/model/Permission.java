package com.softserveinc.ita.homeproject.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Role> roles;

    @Column(name = "name")
    private String name;

}
