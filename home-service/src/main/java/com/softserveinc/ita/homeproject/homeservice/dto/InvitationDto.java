package com.softserveinc.ita.homeproject.homeservice.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationDto {

    private String email;

    private BigDecimal ownershipPart;
}
