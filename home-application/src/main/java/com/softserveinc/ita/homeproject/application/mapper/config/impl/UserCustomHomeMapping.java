package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.CustomHomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import com.softserveinc.ita.homeproject.model.UpdateUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserCustomHomeMapping implements CustomHomeMappingConfig {
    @Override
    public void addMapping(ModelMapper modelMapper) {
        modelMapper.typeMap(UserDto.class, UpdateUser.class).
                addMapping(UserDto::getContacts, UpdateUser::setContacts);
    }
}
