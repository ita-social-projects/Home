package com.softserveinc.ita.homeproject.homedata.general.entity.contact;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.cooperation.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.general.entity.contact.converters.ContactTypeAttributeConverter;
import com.softserveinc.ita.homeproject.homedata.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "contacts")
@SequenceGenerator(name = "sequence", sequenceName = "contacts_sequence")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Contact extends BaseEntity {

    @Convert(converter = ContactTypeAttributeConverter.class)
    @Column(name = "type", insertable = false, updatable = false)
    private ContactType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperation_id")
    private Cooperation cooperation;

    @Column(name = "main")
    private Boolean main;

    @Column(name = "enabled")
    private Boolean enabled;
}
