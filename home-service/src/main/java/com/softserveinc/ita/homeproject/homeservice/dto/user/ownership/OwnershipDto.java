package com.softserveinc.ita.homeproject.homeservice.dto.user.ownership;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.UserDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class OwnershipDto extends BaseDto {

    private UserDto user;

    private String ownershipPart;
}
