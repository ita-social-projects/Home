package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

}
