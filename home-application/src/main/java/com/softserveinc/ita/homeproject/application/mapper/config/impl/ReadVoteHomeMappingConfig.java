package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadVoteDto;
import com.softserveinc.ita.homeproject.model.ReadVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadVoteHomeMappingConfig implements HomeMappingConfig<ReadVoteDto, ReadVote> {

    @Override
    public void addMappings(TypeMap<ReadVoteDto, ReadVote> typeMap) {
        typeMap
            .addMappings(m -> m.map(ReadVoteDto::getId, ReadVote::setId))
            .addMappings(m -> m.map(ReadVoteDto::getQuestionVoteDtos, ReadVote::setQuestionVotes));
    }
}
