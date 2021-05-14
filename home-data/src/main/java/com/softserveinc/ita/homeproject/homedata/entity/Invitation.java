package com.softserveinc.ita.homeproject.homedata.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invitations")
@SequenceGenerator(name = "sequence", sequenceName = "invitations_sequence")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Invitation extends BaseEntity {

    @Column(name = "email")
    private String email;

    @Convert(converter = InvitationStatusAttributeConverter.class)
    @Column(name = "status")
    private InvitationStatus status;

    @Column(name = "sent_datetime")
    private LocalDateTime sentDatetime;
}
