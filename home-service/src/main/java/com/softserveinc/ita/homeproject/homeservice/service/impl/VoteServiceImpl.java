package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.softserveinc.ita.homeproject.homedata.entity.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import com.softserveinc.ita.homeproject.homedata.entity.PollStatus;
import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import com.softserveinc.ita.homeproject.homedata.entity.Vote;
import com.softserveinc.ita.homeproject.homedata.repository.AnswerVariantRepository;
import com.softserveinc.ita.homeproject.homedata.repository.PollRepository;
import com.softserveinc.ita.homeproject.homedata.repository.QuestionVoteRepository;
import com.softserveinc.ita.homeproject.homedata.repository.VoteRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.VoteDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final PollRepository pollRepository;

    private final VoteRepository voteRepository;

    private final QuestionVoteRepository questionVoteRepository;

    private final AnswerVariantRepository answerVariantRepository;

    private final ServiceMapper mapper;

    @Override
    @Transactional
    public VoteDto createVote(Long pollId, VoteDto voteDto) {
        var poll = pollRepository.findById(pollId)
            .filter(Poll::getEnabled)
            .filter(p -> p.getStatus().equals(PollStatus.DRAFT))
            .orElseThrow(() ->
                new NotFoundHomeException(
                    String.format("Poll with 'id: %d' is not found", pollId)));
        voteDto.setPollId(poll.getId());
        var newVote = mapper.convert(voteDto, Vote.class);
        var savedVote = voteRepository.save(newVote);
        for (QuestionVote qv : savedVote.getQuestionVotes()) {
            qv.setVote(savedVote);
            questionVoteRepository.save(qv);
        }
        return mapper.convert(savedVote, VoteDto.class);
    }

    public List<AnswerVariant> getAnswerVariantsByIdList(List<Long> answerIds) {
        return answerVariantRepository.findAllByIdIn(answerIds);
    }
}
