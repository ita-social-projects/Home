package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cooperation")
public class Cooperation extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "usreo")
    private String USREO;

    @Column(name = "iban")
    private String IBAN;

    @Column(name = "registerDate")
    private LocalDateTime registerDate;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address addressCooperation;

    @OneToMany(mappedBy = "cooperation")
    private Set<EmailCooperation> emailCooperations;

    @OneToMany(mappedBy = "cooperation")
    private Set<PhoneCooperation> phoneCooperations;

    @OneToMany(mappedBy = "cooperation")
    private Set<House> houses;

    @Column(name = "isActive")
    private Boolean isActive;

}
