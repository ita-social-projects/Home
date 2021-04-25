package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
public abstract class Invitation extends BaseEntity {


    @Column(name = "email")
    private String email;

//    @Convert(converter = InvitationStatusAttributeConverter.class)
    @Column(name = "status", insertable = false, updatable = false)
    private InvitationStatus status;

    //TODO: It is needed to complete when Cooperation entity is done
//    @ManyToOne
//    @JoinColumn(name="cooperation_id", nullable=false)
//    private Cooperation cooperation;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}