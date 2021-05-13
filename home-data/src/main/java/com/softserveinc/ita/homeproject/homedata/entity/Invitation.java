package com.softserveinc.ita.homeproject.homedata.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Invitation extends BaseEntity {

    @Column(name = "email")
    private String email;

    @Convert(converter = InvitationStatusAttributeConverter.class)
    @Column(name = "status")
    private InvitationStatus status;

    @Column(name = "sent_datetime")
    private LocalDateTime sentDatetime;
}
