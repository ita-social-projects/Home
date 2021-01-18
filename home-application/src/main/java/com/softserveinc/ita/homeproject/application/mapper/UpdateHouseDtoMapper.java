package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.model.UpdateHouse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateHouseDtoMapper extends ViewToDtoMapper<UpdateHouse, HouseDto>{

    HouseDto convertViewToDto(UpdateHouse updateHouse);
}
