package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailDto extends BaseDto {

    private String name;
    private String email;
    private String roleName;
    private String cooperationName;
    private String link;
}
