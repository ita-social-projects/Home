package com.softserveinc.ita.homeproject.homeservice.dto;

import com.softserveinc.ita.homeproject.homedata.entity.Addresses;
import com.softserveinc.ita.homeproject.homedata.entity.Email;
import com.softserveinc.ita.homeproject.homedata.entity.Houses;
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

    private Addresses addressCooperation;

    private List<Houses> houses;

    private String USREO;

    private String IBAN;
}
