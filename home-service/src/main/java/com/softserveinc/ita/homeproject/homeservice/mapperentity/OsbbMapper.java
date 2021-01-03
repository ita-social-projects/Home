package com.softserveinc.ita.homeproject.homeservice.mapperentity;

import com.softserveinc.ita.homeproject.homedata.entity.Osbb;
import com.softserveinc.ita.homeproject.homeservice.dto.OsbbDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OsbbMapper extends EntityToDtoMapper<OsbbDto, Osbb>{

    Osbb convertDtoToEntity(OsbbDto osbbDto);

    OsbbDto convertEntityToDto(Osbb osbb);
}
