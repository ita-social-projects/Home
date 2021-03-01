package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Address extends BaseEntity {

    @Column
    private String region;

    @Column
    private String city;

    @Column
    private String district;

    @Column
    private String street;

    @Column
    private String houseBlock;

    @Column
    private String houseNumber;

    @Column
    private String zipCode;

    @OneToOne
    private Cooperation cooperation;

    @OneToOne
    private House houses;
}
