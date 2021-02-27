package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("contactEmail")
public class Email extends Contact {

    private String contactEmail;
}
