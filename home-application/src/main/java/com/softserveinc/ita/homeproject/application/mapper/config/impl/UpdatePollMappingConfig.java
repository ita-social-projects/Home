package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.application.model.UpdatePoll;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePollMappingConfig implements HomeMappingConfig<UpdatePoll, PollDto> {

    @Override
    public void addMappings(TypeMap<UpdatePoll, PollDto> typeMap) {
        typeMap.addMapping(UpdatePoll::getIncluded, PollDto::setIncluded);
    }
}
