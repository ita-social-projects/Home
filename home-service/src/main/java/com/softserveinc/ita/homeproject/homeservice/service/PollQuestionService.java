package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.PollQuestion;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;

public interface PollQuestionService extends QueryableService<PollQuestion, PollQuestionDto>  {

    PollQuestionDto createPollQuestion(Long pollId, PollQuestionDto pollQuestionDto);

    PollQuestionDto updatePollQuestion(Long pollId, Long id, PollQuestionDto updatePollQuestion);

    void deactivatePollQuestion(Long pollId, Long pollQuestionId);
}
