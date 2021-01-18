package com.softserveinc.ita.homeproject.homeservice.mapperentity;

import com.softserveinc.ita.homeproject.homedata.entity.Houses;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HouseMapper extends EntityToDtoMapper<HouseDto, Houses> {

    Houses convertDtoToEntity(HouseDto houseDto);

    HouseDto convertEntityToDto(Houses houses);
}
