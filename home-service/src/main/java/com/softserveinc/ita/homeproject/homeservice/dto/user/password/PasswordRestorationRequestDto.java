package com.softserveinc.ita.homeproject.homeservice.dto.user.password;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRestorationRequestDto extends BaseDto {

    private String email;
}
