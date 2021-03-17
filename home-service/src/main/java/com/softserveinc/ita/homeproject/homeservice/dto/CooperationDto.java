package com.softserveinc.ita.homeproject.homeservice.dto;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CooperationDto extends BaseDto {

    private String name;

    private Address address;

    private List<HouseDto> houses;

    private String usreo;

    private String iban;
}
