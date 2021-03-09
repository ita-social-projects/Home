package com.softserveinc.ita.homeproject.homedata.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cooperation")
public class Cooperation extends BaseEntity {

    @Column(name = "usreo")
    private String usreo;

    @Column(name = "name")
    private String name;

    @Column(name = "iban")
    private String iban;

    @Column(name = "registerDate")
    private LocalDate registerDate;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToOne(cascade= CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Transient
    private List<House> houses;

}
