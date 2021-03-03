package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("email")
public class EmailContact extends Contact {

    private String email;
}
