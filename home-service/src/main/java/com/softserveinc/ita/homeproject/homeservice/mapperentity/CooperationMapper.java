package com.softserveinc.ita.homeproject.homeservice.mapperentity;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CooperationMapper extends EntityToDtoMapper<CooperationDto, Cooperation>{

    Cooperation convertDtoToEntity(CooperationDto cooperationDto);

    CooperationDto convertEntityToDto(Cooperation cooperation);
}
