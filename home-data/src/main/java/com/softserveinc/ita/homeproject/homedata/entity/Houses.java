package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Houses extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "cooperation_id")
    private Cooperation cooperation;

    @Column(name = "quantity_flat")
    private Integer quantityFlat;

    @Column(name = "house_area")
    private String houseArea;

    @Column(name = "adjoining_area")
    private Integer adjoiningArea;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Addresses addressCooperation;


}
