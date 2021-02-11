package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "invitations")
public class Invitation extends BaseEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private boolean status;

    @Column(name = "sent_datetime")
    private LocalDateTime sentDateTime;

    //TODO: It is needed to complete when Cooperation entity is done
//    @ManyToOne
//    @JoinColumn(name="cooperation_id", nullable=false)
//    private Cooperation cooperation;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
