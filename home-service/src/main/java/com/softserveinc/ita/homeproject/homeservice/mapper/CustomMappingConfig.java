/*
package com.softserveinc.ita.homeproject.homeservice.mapper;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homeservice.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

public interface CustomMappingConfig<D, T> {
    void addMapping(ModelMapper mapper);
}

abstract class AbstractCustomMappingConfig<D, T> implements CustomMappingConfig<D, T> {

    private Class<D> getDtoClass() {
        return null;
    }

    private Class<T> getEntityClass() {
        return null;
    }

    protected abstract void addMappingImpl(TypeMap<D, T> typeMap);

    @Override
    public void addMapping(ModelMapper mapper) {
        addMappingImpl(mapper.typeMap(getDtoClass(), getEntityClass()));
    }
}

@Component
class UserCustomMappingConfig extends AbstractCustomMappingConfig<User, UserDto> {

    @Override
    protected void addMappingImpl(TypeMap<User, UserDto> typeMap) {
        typeMap.addMapping(User::getContacts, UserDto::setContacts);
        // modelMapper.typeMap(User.class, UserDto.class).addMapping(User::getContacts, UserDto::setContacts);
    }
}
 */