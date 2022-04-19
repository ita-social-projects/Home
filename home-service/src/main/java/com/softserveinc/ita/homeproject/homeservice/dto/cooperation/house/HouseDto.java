package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house;

import com.softserveinc.ita.homeproject.homedata.general.address.Address;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class HouseDto extends BaseDto {

    private Integer quantityFlat;

    private Double houseArea;

    private Integer adjoiningArea;

    private Address address;
}
