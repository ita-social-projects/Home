package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Osbb extends BaseEntity {

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "shortName")
    private String shortName;

    @Column(name = "code")
    private String code;

    @Column(name = "account")
    private String account;

    @Column(name = "registerDate")
    private LocalDateTime registerDate;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @Column(name = "managerPerson")
    private String managerPerson;

    @Column(name = "address")
    private String address;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "isActive")
    private Boolean isActive;


}
