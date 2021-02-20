package com.softserveinc.ita.homeproject.homedata.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission extends BaseEntity {

    @ManyToMany(mappedBy = "permissions")
    private List<Role> roles;

    @Column(name = "name")
    private String name;

}
