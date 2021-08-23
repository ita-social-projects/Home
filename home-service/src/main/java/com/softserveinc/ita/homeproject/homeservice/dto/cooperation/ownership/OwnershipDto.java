package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.ownership;

import java.math.BigDecimal;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.UserDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OwnershipDto extends BaseDto {

    private UserDto user;

    private BigDecimal ownershipPart;
}
