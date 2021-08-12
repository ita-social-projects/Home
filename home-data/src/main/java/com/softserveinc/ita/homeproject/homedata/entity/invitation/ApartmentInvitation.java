package com.softserveinc.ita.homeproject.homedata.entity.invitation;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.softserveinc.ita.homeproject.homedata.entity.apartment.Apartment;
import com.softserveinc.ita.homeproject.homedata.entity.invitation.Invitation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("apartment")
public class ApartmentInvitation extends Invitation {

    @Column(name = "ownership_part")
    private BigDecimal ownershipPart;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
}
