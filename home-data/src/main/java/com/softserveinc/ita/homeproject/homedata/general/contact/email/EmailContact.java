package com.softserveinc.ita.homeproject.homedata.general.contact.email;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("email")
public class EmailContact extends Contact {

    private String email;
}
