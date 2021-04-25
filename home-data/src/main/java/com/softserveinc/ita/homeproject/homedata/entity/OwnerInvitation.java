package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "owner_invitations")
@SequenceGenerator(name = "sequence", sequenceName = "owner_invitations_sequence")
public class OwnerInvitation extends Invitation {


    @Column(name = "ownership_area")
    private BigDecimal ownershipArea;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

}
