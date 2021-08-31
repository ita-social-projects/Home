package com.softserveinc.ita.homeproject.homedata.user.entity.ownership;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.cooperation.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.entity.apartment.Apartment;
import com.softserveinc.ita.homeproject.homedata.user.entity.User;
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
