package com.softserveinc.ita.homeproject.homedata.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invitations")
@SequenceGenerator(name = "sequence", sequenceName = "invitations_sequence")
public class ApartmentInvitation extends Invitation {

    @Column(name = "ownership_part")
    private BigDecimal ownershipPart;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
}
