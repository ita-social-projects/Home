package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.model.CreateHouse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateHouseDtoMapper extends ViewToDtoMapper<CreateHouse, HouseDto> {

    HouseDto convertViewToDto(CreateHouse createHouse);
}
