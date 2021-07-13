package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Vote;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadVoteDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadVoteServiceMappingConfig implements ServiceMappingConfig<Vote, ReadVoteDto> {

    @Override
    public void addMappings(TypeMap<Vote, ReadVoteDto> typeMap) {
        typeMap.addMappings(mapper -> mapper.map(Vote::getId, ReadVoteDto::setId))
            .addMappings(mapper -> mapper.map(Vote::getQuestionVotes, ReadVoteDto::setQuestionVoteDtos));
    }
}
