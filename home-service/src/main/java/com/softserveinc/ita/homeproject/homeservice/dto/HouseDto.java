package com.softserveinc.ita.homeproject.homeservice.dto;

import com.softserveinc.ita.homeproject.homedata.entity.Address;
import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class HouseDto extends BaseDto {

    private Integer quantityFlat;

    private String houseArea;

    private Integer adjoiningArea;

    private Address address;
}
