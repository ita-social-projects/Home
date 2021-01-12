package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class House extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "house_id")
    private Cooperation cooperation;

    @Column(name = "area")
    private int area;
}
