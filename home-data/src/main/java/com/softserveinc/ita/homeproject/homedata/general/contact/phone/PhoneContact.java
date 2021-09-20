package com.softserveinc.ita.homeproject.homedata.general.contact.phone;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("phone")
public class PhoneContact extends Contact {

    private String phone;
}
