package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue("contactPhone")
public class Phone extends Contact {

    @Column
    private String contactPhone;
}
