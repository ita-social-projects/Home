package com.softserveinc.ita.homeproject.homedata.entity.ownership;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.entity.apartment.Apartment;
import com.softserveinc.ita.homeproject.homedata.entity.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ownerships")
@SequenceGenerator(name = "sequence", sequenceName = "ownerships_sequence")
public class Ownership extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "ownership_part")
    private BigDecimal ownershipPart;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    @Column(name = "enabled")
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "cooperation_id", referencedColumnName = "id")
    private Cooperation cooperation;

}
