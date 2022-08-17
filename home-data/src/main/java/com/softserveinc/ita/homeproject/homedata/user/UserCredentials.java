package com.softserveinc.ita.homeproject.homedata.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "user_credentials")
public class UserCredentials extends BaseEntity {

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "userCredentials")
    private User user;

}
