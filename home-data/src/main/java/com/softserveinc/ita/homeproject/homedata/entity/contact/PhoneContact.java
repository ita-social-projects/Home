package com.softserveinc.ita.homeproject.homedata.entity.contact;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("phone")
public class PhoneContact extends Contact {

    private String phone;
}
