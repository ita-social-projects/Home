package com.softserveinc.ita.homeproject.application.api;


import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.MANAGE_POLLS;
import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.READ_POLL;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.model.CreateQuestion;
import com.softserveinc.ita.homeproject.application.model.CreateVote;
import com.softserveinc.ita.homeproject.application.model.HouseLookup;
import com.softserveinc.ita.homeproject.application.model.PollStatus;
import com.softserveinc.ita.homeproject.application.model.PollType;
import com.softserveinc.ita.homeproject.application.model.QuestionType;
import com.softserveinc.ita.homeproject.application.model.ReadHouse;
import com.softserveinc.ita.homeproject.application.model.ReadPoll;
import com.softserveinc.ita.homeproject.application.model.ReadQuestion;
import com.softserveinc.ita.homeproject.application.model.ReadVote;
import com.softserveinc.ita.homeproject.application.model.UpdateQuestion;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.VoteDto;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.house.HouseService;
import com.softserveinc.ita.homeproject.homeservice.service.poll.PollService;
import com.softserveinc.ita.homeproject.homeservice.service.poll.house.PollHouseService;
import com.softserveinc.ita.homeproject.homeservice.service.poll.question.PollQuestionService;
import com.softserveinc.ita.homeproject.homeservice.service.poll.vote.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Provider
@Component
public class PollApiImpl extends CommonApi implements PollsApi {
    @Autowired
    private PollService pollService;

    @Autowired
    private PollHouseService housePollService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private PollQuestionService pollQuestionService;

    @Autowired
    private VoteService voteService;

    @PreAuthorize(MANAGE_POLLS)
    @Override
    public Response createPolledHouse(Long pollId, HouseLookup houseLookup) {
        var lookupPolledHouseDto = mapper.convert(houseLookup, HouseDto.class);
        housePollService.add(lookupPolledHouseDto.getId(), pollId);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(MANAGE_POLLS)
    @Override
    public Response createQuestion(Long pollId, CreateQuestion createQuestion) {
        var createQuestionDto = mapper.convert(createQuestion, PollQuestionDto.class);
        createQuestionDto.setPollId(pollId);
        var readQuestionDto = pollQuestionService.createPollQuestion(createQuestionDto);
        var readQuestion = mapper.convert(readQuestionDto, ReadQuestion.class);

        return Response.status(Response.Status.CREATED).entity(readQuestion).build();
    }

    @PreAuthorize(MANAGE_POLLS)
    @Override
    public Response deletePolledHouse(Long pollId, Long id) {
        housePollService.remove(id, pollId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(MANAGE_POLLS)
    @Override
    public Response deleteQuestion(Long pollId, Long id) {
        pollQuestionService.deactivatePollQuestion(pollId, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(READ_POLL)
    @Override
    public Response getPoll(Long id) {
        var pollDto = pollService.getOne(id);
        var readPoll = mapper.convert(pollDto, ReadPoll.class);
        return Response.status(Response.Status.OK).entity(readPoll).build();
    }

    @PreAuthorize(READ_POLL)
    @Override
    public Response getPolledHouse(Long pollId, Long id) {
        var toGet = houseService.getOne(id, getSpecification());
        var readHouse = mapper.convert(toGet, ReadHouse.class);

        return Response.status(Response.Status.OK).entity(readHouse).build();
    }

    @PreAuthorize(READ_POLL)
    @Override
    public Response getQuestion(Long pollId, Long id) {
        PollQuestionDto toGet = pollQuestionService.getOne(id, getSpecification());
        var readQuestion = mapper.convert(toGet, ReadQuestion.class);

        return Response.status(Response.Status.OK).entity(readQuestion).build();
    }

    @PreAuthorize(READ_POLL)
    @Override
    public Response queryPoll(Long cooperationId,
                              Integer pageNumber,
                              Integer pageSize,
                              String sort,
                              String filter,
                              Long id,
                              LocalDateTime creationDate,
                              LocalDateTime completionDate,
                              PollType type,
                              PollStatus status) {
        Page<PollDto> readPoll = pollService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readPoll, ReadPoll.class);
    }

    @PreAuthorize(READ_POLL)
    @Override
    public Response queryPolledHouse(Long pollId,
                                     Integer pageNumber,
                                     Integer pageSize,
                                     String sort,
                                     String filter,
                                     Long id,
                                     Integer quantityFlat,
                                     Integer adjoiningArea,
                                     BigDecimal houseArea) {
        verifyExistence(pollId, pollService);
        Page<HouseDto> readHouse = houseService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readHouse, ReadHouse.class);
    }

    @PreAuthorize(READ_POLL)
    @Override
    public Response queryQuestion(Long pollId,
                                  Integer pageNumber,
                                  Integer pageSize,
                                  String sort,
                                  String filter,
                                  Long id,
                                  QuestionType type) {
        verifyExistence(pollId, pollService);
        Page<PollQuestionDto> readQuestion = pollQuestionService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readQuestion, ReadQuestion.class);
    }

    @PreAuthorize(MANAGE_POLLS)
    @Override
    public Response updateQuestion(Long pollId, Long id, UpdateQuestion updateQuestion) {
        var updateQuestionDto = mapper.convert(updateQuestion, PollQuestionDto.class);
        updateQuestionDto.setId(id);
        updateQuestionDto.setPollId(pollId);
        var toUpdate = pollQuestionService.updatePollQuestion(updateQuestionDto);
        var readQuestion = mapper.convert(toUpdate, ReadQuestion.class);

        return Response.status(Response.Status.OK).entity(readQuestion).build();
    }

    @PreAuthorize(MANAGE_POLLS)
    @Override
    public Response createVote(Long pollId, CreateVote createVote) {
        var createVoteDto = mapper.convert(createVote, VoteDto.class);
        createVoteDto.setPollId(pollId);
        var readVoteDto = voteService.createVote(createVoteDto);
        var readVote = mapper.convert(readVoteDto, ReadVote.class);

        return Response.status(Response.Status.CREATED).entity(readVote).build();
    }
}
