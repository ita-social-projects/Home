package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;

    @Column(name = "name")
    private String name;

}
