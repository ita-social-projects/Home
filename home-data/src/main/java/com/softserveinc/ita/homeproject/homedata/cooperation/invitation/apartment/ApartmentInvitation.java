package com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.softserveinc.ita.homeproject.homedata.cooperation.apatment.Apartment;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("apartment")
public class ApartmentInvitation extends Invitation {

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
}

