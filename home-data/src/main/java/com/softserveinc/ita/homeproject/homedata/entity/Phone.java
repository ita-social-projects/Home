package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("phone")
public class Phone extends Contact {

    @Column
    private String phone;
}
