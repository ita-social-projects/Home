package com.softserveinc.ita.homeproject.homeservice.dto.cooperation;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.house.apartment.address.Address;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house.HouseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CooperationDto extends BaseDto {

    private String name;

    private Address address;

    private List<HouseDto> houses;

    private List<ContactDto> contacts;

    private String usreo;

    private String iban;

    private String adminEmail;
}
