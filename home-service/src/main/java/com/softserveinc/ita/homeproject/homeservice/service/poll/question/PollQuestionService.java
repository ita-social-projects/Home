package com.softserveinc.ita.homeproject.homeservice.service.poll.question;

import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface PollQuestionService extends QueryableService<PollQuestion, PollQuestionDto> {

    PollQuestionDto createPollQuestion(Long pollId, PollQuestionDto pollQuestionDto);

    PollQuestionDto updatePollQuestion(Long pollId, Long id, PollQuestionDto updatePollQuestion);

    void deactivatePollQuestion(Long pollId, Long pollQuestionId);
}
