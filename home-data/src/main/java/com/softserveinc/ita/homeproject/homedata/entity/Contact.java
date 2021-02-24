package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "contact_type",
        discriminatorType = DiscriminatorType.STRING)
public abstract class Contact extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", insertable = false, updatable = false)
    private ContactType contactType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperation_id")
    private Cooperation cooperation;

}
