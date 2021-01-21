package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cooperation")
public class Cooperation extends BaseEntity {


    @Column(name = "usreo")
    private String USREO;

    @Column(name = "name")
    private String name;

    @Column(name = "iban")
    private String IBAN;

    @Column(name = "registerDate")
    private LocalDateTime registerDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Addresses addressesCooperation;

    @OneToMany
    @JoinTable(
            name = "cooperation_email",
            joinColumns = {@JoinColumn(name = "cooperation_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "email_id", referencedColumnName = "id")}
    )
    private List<Email> emails;

    @OneToMany
    @JoinTable(
            name = "cooperation_phone",
            joinColumns = {@JoinColumn(name = "cooperation_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "phone_id", referencedColumnName = "id")}
    )
    private List<Phone> phones;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Houses> houses;

    @Column(name = "enabled")
    private Boolean enabled;
    @Column(name="updateDate")
    private LocalDateTime updateDate;

}
