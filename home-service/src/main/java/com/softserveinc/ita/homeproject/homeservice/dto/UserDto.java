package com.softserveinc.ita.homeproject.homeservice.dto;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private List<Contact> contacts;
}
