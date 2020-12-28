package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UpdateUserDtoMapper extends ViewToDtoMapper<UpdateUser, UserDto> {

    UserDto convertViewToDto(UpdateUser updateUser);
}
