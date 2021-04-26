package com.softserveinc.ita.homeproject.homedata.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "ownerships")
@SequenceGenerator(name = "sequence", sequenceName = "ownerships_sequence")
public class Ownership extends BaseEntity {

    @Convert(converter = OwnershipStatusAttributeConverter.class)
    @Column(name = "status", insertable = false, updatable = false)
    private OwnershipStatus status;

    @Column(name = "ownership_area")
    private double ownershipArea;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @Column(name = "enabled")
    private Boolean enabled;

}
