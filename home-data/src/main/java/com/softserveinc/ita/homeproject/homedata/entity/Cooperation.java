package com.softserveinc.ita.homeproject.homedata.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cooperation")
public class Cooperation extends BaseEntity {

    @Column(name = "usreo")
    private String usreo;

    @Column(name = "name")
    private String name;

    @Column(name = "iban")
    private String iban;

    @Column(name = "registerDate")
    private LocalDateTime registerDate;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany
    private List<House> houses;
}
