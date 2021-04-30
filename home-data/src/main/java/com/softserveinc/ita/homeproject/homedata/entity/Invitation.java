package com.softserveinc.ita.homeproject.homedata.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invitations")
@SequenceGenerator(name = "sequence", sequenceName = "invitations_sequence")
public class Invitation extends BaseEntity {

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Convert(converter = InvitationStatusAttributeConverter.class)
    @Column(name = "status")
    private InvitationStatus status;

    @Column(name = "ownership_part")
    private BigDecimal ownershipPart;

    @Column(name = "sent_datetime")
    private LocalDateTime sentDatetime;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

}
