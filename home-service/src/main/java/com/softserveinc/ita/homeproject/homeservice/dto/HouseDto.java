package com.softserveinc.ita.homeproject.homeservice.dto;

import com.softserveinc.ita.homeproject.homedata.entity.Addresses;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseDto extends BaseDto {

    private Integer quantityFlat;

    private String houseArea;

    private Integer adjoiningArea;

}
