package com.softserveinc.ita.homeproject.homeservice.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnershipDto extends BaseDto {

    private UserDto user;

    private BigDecimal ownershipPart;
}
