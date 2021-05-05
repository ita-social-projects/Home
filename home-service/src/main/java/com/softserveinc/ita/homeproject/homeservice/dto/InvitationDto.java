package com.softserveinc.ita.homeproject.homeservice.dto;

import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.entity.Role;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class InvitationDto extends BaseDto {

    private String name;
    private String email;
    private boolean status;
    private LocalDateTime sentDateTime;
    private Role role;
    private Cooperation cooperation;
}
