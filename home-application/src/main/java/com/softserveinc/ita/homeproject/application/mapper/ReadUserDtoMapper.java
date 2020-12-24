package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.model.ReadUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReadUserDtoMapper extends DtoToViewMapper<UserDto, ReadUser> {

    ReadUser convertDtoToView(UserDto userDto);
}
