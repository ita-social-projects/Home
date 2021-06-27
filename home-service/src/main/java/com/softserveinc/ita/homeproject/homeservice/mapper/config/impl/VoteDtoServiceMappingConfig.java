package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import com.softserveinc.ita.homeproject.homedata.entity.Vote;
import com.softserveinc.ita.homeproject.homedata.repository.PollRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.VoteDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteDtoServiceMappingConfig implements ServiceMappingConfig<VoteDto, Vote> {

    private final PollRepository pollRepository;

    @Override
    public void addMappings(TypeMap<VoteDto, Vote> typeMap) {
        typeMap
            .addMappings(m -> m.skip(Vote::setPoll))
            .addMappings(mapper -> mapper.map(VoteDto::getQuestionVoteDtos, Vote::setQuestionVotes))
            .setPostConverter(pollConverter());
    }

    public Converter<VoteDto, Vote> pollConverter() {
        return context -> {
            VoteDto source = context.getSource();
            Vote destination = context.getDestination();
            Long pollId = source.getPollId();
            if (pollId != null) {
                Poll poll = pollRepository.findById(pollId).orElse(null);
                destination.setPoll(poll);
            }
            return context.getDestination();
        };
    }
}
