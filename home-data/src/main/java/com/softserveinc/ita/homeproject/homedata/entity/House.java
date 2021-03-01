package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class House extends BaseEntity {

    @Column(name = "quantity_flat")
    private Integer quantityFlat;

    @Column(name = "house_area")
    private String houseArea;

    @Column(name = "adjoining_area")
    private Integer adjoiningArea;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "cooperation_id")
    private Cooperation cooperation;
}
