package com.softserveinc.ita.homeproject.application.api;

import java.math.BigDecimal;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.api.PollsApi;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.service.HousePollService;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import com.softserveinc.ita.homeproject.model.HouseLookup;
import com.softserveinc.ita.homeproject.model.ReadHouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

public class PollsApiImpl extends CommonApi implements PollsApi {

    @Autowired
    private HousePollService housePollService;

    @Autowired
    private HouseService houseService;

    @Override
    public Response createPolledHouse(Long pollId, HouseLookup houseLookup) {
        var lookupPolledHouseDto = mapper.convert(houseLookup, HouseDto.class);
        var readPolledHouseDto = housePollService.add(lookupPolledHouseDto.getId(), pollId);
        var readPolledHouse = mapper.convert(readPolledHouseDto, ReadHouse.class);

        return Response.status(Response.Status.CREATED).entity(readPolledHouse).build();
    }

    @Override
    public Response deletePolledHouse(Long pollId, Long id) {
        housePollService.deactivate(id, pollId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response getPolledHouse(Long pollId, Long id) {
        HouseDto toGet = houseService.getOne(id, getSpecification());
        ReadHouse readHouse = mapper.convert(toGet, ReadHouse.class);

        return Response.status(Response.Status.OK).entity(readHouse).build();
    }

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
        Page<HouseDto> readHouse = houseService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readHouse, ReadHouse.class);
    }
}