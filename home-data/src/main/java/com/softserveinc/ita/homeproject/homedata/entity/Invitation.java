package com.softserveinc.ita.homeproject.homedata.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne
    @JoinColumn(name="cooperation_id", nullable=false)
    private Cooperation cooperation;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
