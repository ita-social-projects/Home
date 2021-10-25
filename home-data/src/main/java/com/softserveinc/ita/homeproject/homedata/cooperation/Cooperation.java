
package com.softserveinc.ita.homeproject.homedata.cooperation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.cooperation.house.House;
import com.softserveinc.ita.homeproject.homedata.general.address.Address;
import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.user.UserCooperation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cooperations")
@SequenceGenerator(name = "sequence", sequenceName = "cooperations_sequence")
public class Cooperation extends BaseEntity {

    @Column(name = "usreo")
    private String usreo;

    @Column(name = "name")
    private String name;

    @Column(name = "iban")
    private String iban;

    @Column(name = "register_date")
    private LocalDate registerDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "cooperation", cascade = CascadeType.PERSIST)
    private List<House> houses;

    @OneToMany(mappedBy = "cooperation", cascade = {CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)

    private List<Contact> contacts;

    @OneToMany(mappedBy = "cooperation", cascade = CascadeType.PERSIST)
    private List<Poll> polls;

    @OneToMany(mappedBy = "cooperation", cascade = CascadeType.PERSIST)
    private List<UserCooperation> userCooperations;
}
