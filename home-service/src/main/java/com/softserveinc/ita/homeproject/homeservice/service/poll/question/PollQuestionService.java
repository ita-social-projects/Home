package com.softserveinc.ita.homeproject.homeservice.service.poll.question;

import com.softserveinc.ita.homeproject.homedata.entity.polls.templates.PollQuestion;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.templates.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface PollQuestionService extends QueryableService<PollQuestion, PollQuestionDto> {

    PollQuestionDto createPollQuestion(PollQuestionDto pollQuestionDto);

    PollQuestionDto updatePollQuestion(PollQuestionDto updatePollQuestion);

    void deactivatePollQuestion(Long pollId, Long pollQuestionId);
}
