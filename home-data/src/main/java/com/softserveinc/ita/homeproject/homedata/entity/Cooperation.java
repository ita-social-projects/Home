package com.softserveinc.ita.homeproject.homedata.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cooperation")
@SequenceGenerator(name = "sequence", sequenceName = "cooperation_sequence")
public class Cooperation extends BaseEntity implements Serializable {

    @OneToMany(mappedBy = "cooperation")
    private List<Contact> contacts;
}
