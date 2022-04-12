package com.softserveinc.ita.homeproject.homeservice.dto.user.ownership;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.UserDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OwnershipDto extends BaseDto {

    private UserDto user;

    private String ownershipPart;
}
