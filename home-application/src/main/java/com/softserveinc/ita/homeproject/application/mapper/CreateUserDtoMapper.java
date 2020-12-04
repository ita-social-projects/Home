package com.softserveinc.ita.homeproject.application.mapper;

import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.model.CreateUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreateUserDtoMapper extends ViewToDtoMapper<CreateUser, UserDto>{

    UserDto convertViewToDto(CreateUser createUser);
}
