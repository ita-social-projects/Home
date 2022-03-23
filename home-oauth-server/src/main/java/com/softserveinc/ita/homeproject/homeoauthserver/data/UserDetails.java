package com.softserveinc.ita.homeproject.homeoauthserver.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDetails {

    private String email;

    private String password;
}
