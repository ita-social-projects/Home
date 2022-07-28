package com.softserveinc.ita.homeproject.homeservice.dto.user.password;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PasswordRestorationApprovalDto extends BaseDto {

    private String email;

    private String token;

    private String newPassword;

    private String passwordConfirmation;
}
