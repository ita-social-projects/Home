package com.softserveinc.ita.homeproject.homeservice.dto;

import com.softserveinc.ita.homeproject.homedata.entity.Address;
import com.softserveinc.ita.homeproject.homedata.entity.Email;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.entity.Phone;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CooperationDto extends BaseDto {

    private Long id;

    private String name;

    private Set<Phone> phone;

    private Set<Email> email;

    private Address addressCooperation;

    private Set<House> houses;

    private String USREO;

    private String IBAN;
}
