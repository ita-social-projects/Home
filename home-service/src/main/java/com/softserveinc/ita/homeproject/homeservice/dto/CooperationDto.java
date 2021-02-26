package com.softserveinc.ita.homeproject.homeservice.dto;

import java.util.List;
import com.softserveinc.ita.homeproject.homedata.entity.Address;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CooperationDto extends BaseDto {

    private String name;

    private Address address;

    private List<House> houses;

    private String usreo;

    private String iban;
}
