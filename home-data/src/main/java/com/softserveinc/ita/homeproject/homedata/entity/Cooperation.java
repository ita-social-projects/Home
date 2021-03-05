package com.softserveinc.ita.homeproject.homedata.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Cooperation extends BaseEntity implements Serializable {

    @OneToMany(mappedBy = "cooperation")
    private List<Contact> contacts;
}
