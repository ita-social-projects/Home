package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.VoteDto;
import com.softserveinc.ita.homeproject.model.CreateVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateVoteVoteDtoHomeMappingConfig implements HomeMappingConfig<CreateVote, VoteDto> {

    @Override
    public void addMappings(TypeMap<CreateVote, VoteDto> typeMap) {
        typeMap.addMappings(mapper -> mapper.skip(VoteDto::setPollId))
            .addMappings(mapper -> mapper.map(CreateVote::getQuestionVotes, VoteDto::setQuestionVoteDtos));
    }
}
