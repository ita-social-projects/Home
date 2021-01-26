package com.softserveinc.ita.homeproject.homeservice.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homeservice.config.CustomServiceMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserCustomServiceMapping implements CustomServiceMappingConfig {

    @Override
    public void addMapping(ModelMapper modelMapper) {
        modelMapper.typeMap(User.class, UserDto.class).addMapping(User::getContacts, UserDto::setContacts);
    }

}
