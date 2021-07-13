package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Vote;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateVoteDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateVoteServiceMappingConfig implements ServiceMappingConfig<CreateVoteDto, Vote> {

    @Override
    public void addMappings(TypeMap<CreateVoteDto, Vote> typeMap) {
        typeMap
            .addMappings(mapper -> mapper.map(CreateVoteDto::getPollId, Vote::setPollId))
            .addMappings(mapper -> mapper.map(CreateVoteDto::getQuestionVoteDtos, Vote::setQuestionVotes));
    }
}
