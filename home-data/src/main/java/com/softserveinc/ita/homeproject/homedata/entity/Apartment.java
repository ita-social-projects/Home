package com.softserveinc.ita.homeproject.homedata.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "apartments")
@SequenceGenerator(name = "sequence", sequenceName = "apartments_sequence")
public class Apartment extends BaseEntity{
    @Column(name = "apartment_number")
    private String apartmentNumber;

    @Column(name = "apartment_area")
    private BigDecimal apartmentArea;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "enabled")
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.PERSIST)
    private List<Ownership> ownerships;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.PERSIST)
    private List<ApartmentInvitation> invitations;

}
