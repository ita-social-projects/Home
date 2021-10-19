package com.softserveinc.ita.homeproject.homedata.user;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import com.softserveinc.ita.homeproject.homedata.user.ownership.Ownership;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@SequenceGenerator(name = "sequence", sequenceName = "users_sequence")
public class User extends BaseEntity {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Contact> contacts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Ownership> ownerships;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<UserCooperation> userCooperations;

}
