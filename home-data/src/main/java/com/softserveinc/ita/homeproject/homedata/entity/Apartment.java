package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "apartments")
@SequenceGenerator(name = "sequence", sequenceName = "apartments_sequence")
public class Apartment extends BaseEntity{
    @Column(name = "apartment_number")
    private String apartmentNumber;

    @Column(name = "apartment_area")
    private Double apartmentArea;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "enabled")
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL)
    private Set<Ownership> ownerships;

}
