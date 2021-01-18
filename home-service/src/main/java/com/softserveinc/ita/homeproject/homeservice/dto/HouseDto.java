package com.softserveinc.ita.homeproject.homeservice.dto;

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
//    private Addresses addresses;

}
