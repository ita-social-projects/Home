package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("contactPhone")
public class Phone extends Contact {

    private String contactPhone;
}
