package com.softserveinc.ita.homeproject.homeservice.dto;

import com.softserveinc.ita.homeproject.homedata.entity.Address;
import com.softserveinc.ita.homeproject.homedata.entity.Email;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homedata.entity.Phone;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CooperationDto extends BaseDto {

    private Long id;

    private String name;

    private List<Phone> phone;

    private List<Email> email;

    private Address addressCooperation;

    private List<House> houses;

    private String USREO;

    private String IBAN;
}
