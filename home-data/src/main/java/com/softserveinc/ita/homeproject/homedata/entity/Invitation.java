package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Invitation extends BaseEntity{
    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private boolean status;

    @Column(name = "role")
    private String role;

    @Column(name = "email")
    private String email;
}
