package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.model.ReadCooperation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReadCooperationDtoMapper extends DtoToViewMapper<CooperationDto, ReadCooperation>{

    ReadCooperation convertDtoToView(CooperationDto cooperationDto);
}
