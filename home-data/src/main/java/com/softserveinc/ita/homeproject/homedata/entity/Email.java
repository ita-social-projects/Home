package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("contactEmail")
public class Email extends Contact {

    @Column
    private String contactEmail;
}
