package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OsbbDto extends BaseDto{

    private Long id;

    private String fullName;

    private String shortName;

    private String managerPerson;

    private String phone;

    private String email;

    private String address;

    private String code;

    private String account;
}
