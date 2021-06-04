package com.softserveinc.ita.homeproject.application.api;

import com.softserveinc.ita.homeproject.api.PollsApi;
import com.softserveinc.ita.homeproject.homeservice.dto.PollDto;
import com.softserveinc.ita.homeproject.homeservice.service.PollService;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_POLL_PERMISSION;

@Provider
@Component
public class PollsApiImpl extends CommonApi implements PollsApi {

    @Autowired
    private PollService pollService;

    @PreAuthorize(GET_POLL_PERMISSION)
    @Override
    public Response getPoll(Long id) {
        PollDto pollDto = pollService.getOne(id);
        ReadPoll readPoll = mapper.convert(pollDto, ReadPoll.class);
        return Response.status(Response.Status.OK).entity(readPoll).build();
    }

    @PreAuthorize(GET_POLL_PERMISSION)
    @Override
    public Response queryPoll(
            Integer pageNumber,
            Integer pageSize,
            String sort,
            String filter,
            Long cooperationId,
            Long id
    ) {
        Page<PollDto> readPoll = pollService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readPoll, ReadPoll.class);
    }

}
