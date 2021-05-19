package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailDto extends BaseDto {
    private String email;

    private String role;

    private String cooperationName;

    private String link;

    private InvitationTypeDto type;
}
