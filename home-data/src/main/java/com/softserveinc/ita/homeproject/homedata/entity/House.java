package com.softserveinc.ita.homeproject.homedata.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "houses")
@SequenceGenerator(name = "sequence", sequenceName = "houses_sequence")
public class House extends BaseEntity {

    @Column(name = "quantity_flat")
    private Integer quantityFlat;

    @Column(name = "house_area")
    private Double houseArea;

    @Column(name = "adjoining_area")
    private Integer adjoiningArea;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "cooperation_id")
    private Cooperation cooperation;

    @OneToMany(mappedBy = "house", cascade = CascadeType.PERSIST)
    private List<Apartment> apartments;
}
