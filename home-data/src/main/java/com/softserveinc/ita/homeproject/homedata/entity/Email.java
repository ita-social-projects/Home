package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Email extends BaseEntity {

    @OneToMany(mappedBy = "email")
    private Set<EmailCooperation> emailCooperations;

}
