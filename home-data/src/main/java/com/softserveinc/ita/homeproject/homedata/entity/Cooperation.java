package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cooperation")
public class Cooperation extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "usero")
    private String USERO;

    @Column(name = "iban")
    private String IBAN;

    @Column(name = "registerDate")
    private LocalDateTime registerDate;

    @OneToMany
    private Set<Houses> houses;

}
