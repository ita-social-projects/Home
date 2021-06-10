package com.softserveinc.ita.homeproject.application.api;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.api.PollsApi;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.service.PollQuestionService;
import com.softserveinc.ita.homeproject.model.CreateQuestion;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.model.UpdateQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;


@Provider
@Component
public class PollApiImpl extends CommonApi implements PollsApi {

    @Autowired
    private PollQuestionService pollQuestionService;

    @Override
    public Response createQuestion(Long pollId, @Valid CreateQuestion createQuestion) {
        var createQuestionDto = mapper.convert(createQuestion, PollQuestionDto.class);
        var readQuestionDto = pollQuestionService.createPollQuestion(pollId, createQuestionDto);
        var readQuestion = mapper.convert(readQuestionDto, ReadMultipleChoiceQuestion.class);

        return Response.status(Response.Status.CREATED).entity(readQuestion).build();
    }

    @Override
    public Response deleteQuestion(Long pollId, Long id) {
        pollQuestionService.deactivatePollQuestion(pollId, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response getQuestion(Long pollId, Long id) {
        PollQuestionDto toGet = pollQuestionService.getOne(id, getSpecification());
        var readQuestion = mapper.convert(toGet, ReadMultipleChoiceQuestion.class);

        return Response.status(Response.Status.OK).entity(readQuestion).build();
    }

    @Override
    public Response queryQuestion(@Min(1L) Long pollId,
                                  @Min(1) Integer pageNumber,
                                  @Min(1) @Max(10) Integer pageSize, String sort, String filter, Long id) {

        Page<PollQuestionDto> readQuestion = pollQuestionService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readQuestion, ReadMultipleChoiceQuestion.class);
    }

    @Override
    public Response updateQuestion(Long pollId, Long id, @Valid UpdateQuestion updateQuestion) {
        var updateQuestionDto = mapper.convert(updateQuestion, PollQuestionDto.class);
        var toUpdate = pollQuestionService.updatePollQuestion(pollId, id, updateQuestionDto);
        var readQuestion = mapper.convert(toUpdate, ReadMultipleChoiceQuestion.class);

        return Response.status(Response.Status.OK).entity(readQuestion).build();
    }
}
