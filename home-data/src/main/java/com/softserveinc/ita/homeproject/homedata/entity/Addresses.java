package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Addresses extends BaseEntity {
    @Column
    private String region;

    @Column
    private String city;

    @Column
    private String district;

    @Column
    private String street;

    @Column
    private String housingNumber;

    @Column
    private String houseNumber;

    @Column
    private String zipCode;

    @OneToOne
    private Cooperation cooperation;

    @OneToOne
    private Houses houses;
}
