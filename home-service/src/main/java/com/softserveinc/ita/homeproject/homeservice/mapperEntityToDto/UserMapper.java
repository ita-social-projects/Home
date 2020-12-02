package com.softserveinc.ita.homeproject.homeservice.mapperEntityToDto;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityToDtoMapper<UserDto, User> {

    User convertDtoToEntity(UserDto userDto);

    UserDto convertEntityToDto(User user);
}
