package com.softserveinc.ita.homeproject.homeservice.dto.user.password;

import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homeservice.service.general.email.Mailable;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PasswordRecoveryTokenDto extends Mailable {

    private String email;

    private String token;

    private LocalDateTime sentDateTime;

    private Boolean enabled;
}
