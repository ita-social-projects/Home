package com.softserveinc.ita.homeproject.homeservice.dto.user.password;

import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PasswordRecoveryTokenDto extends BaseDto {

    private String email;

    private String recoveryToken;

    private LocalDateTime sentDateTime;

    private Boolean enabled;
}
