package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Cooperation extends BaseEntity{

    @OneToMany(mappedBy = "cooperation")
    private List<Contact> contacts;
}
