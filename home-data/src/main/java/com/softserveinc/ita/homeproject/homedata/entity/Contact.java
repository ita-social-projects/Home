package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="contact_type",
        discriminatorType = DiscriminatorType.STRING)
public class Contact extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cooperation_id")
    private Cooperation cooperation;

}
