package com.softserveinc.ita.homeproject.homeservice.dto.mail;

import java.math.BigDecimal;
import javax.validation.constraints.Email;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationTypeDto;
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
    @Email
    private String email;

    private String role;

    private String cooperationName;

    private BigDecimal ownershipPat;

    private String apartmentNumber;

    private String link;

    private Boolean isRegistered;

    private InvitationTypeDto type;

    private String registrationToken;
}
