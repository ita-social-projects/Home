package com.softserveinc.ita.homeproject.homeservice.dto.house;

import com.softserveinc.ita.homeproject.homedata.entity.address.Address;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HouseDto extends BaseDto {

    private Integer quantityFlat;

    private Double houseArea;

    private Integer adjoiningArea;

    private Address address;
}
