package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.model.ReadHouse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReadHouseDtoMapper extends DtoToViewMapper<HouseDto, ReadHouse> {
    ReadHouse convertDtoToView(HouseDto houseDto);
}
