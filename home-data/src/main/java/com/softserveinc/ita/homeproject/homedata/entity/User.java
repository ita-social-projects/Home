package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity{

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "locked")
    private Boolean locked;

    @Column(name = "credentials_expired")
    private Boolean credentialsExpired;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "password")
    private String password;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "contacts")
    private String contacts;

    @ManyToMany
    @JoinTable(name = "user_cooperation",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

}
