package com.softserveinc.ita.homeproject.homeservice.config;

import org.modelmapper.ModelMapper;

public interface CustomServiceMappingConfig {
    void addMapping(ModelMapper modelMapper);
}
