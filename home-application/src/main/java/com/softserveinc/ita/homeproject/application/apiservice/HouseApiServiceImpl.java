package com.softserveinc.ita.homeproject.application.apiservice;


import com.softserveinc.ita.homeproject.api.CooperationApiService;
import com.softserveinc.ita.homeproject.application.mapper.CreateHouseDtoMapper;
import com.softserveinc.ita.homeproject.application.mapper.ReadHouseDtoMapper;
import com.softserveinc.ita.homeproject.application.mapper.UpdateHouseDtoMapper;
import com.softserveinc.ita.homeproject.homedata.repository.HousesRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import com.softserveinc.ita.homeproject.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseApiServiceImpl implements CooperationApiService {

    private final HouseService houseService;
    private final CreateHouseDtoMapper createHouseDtoMapper;
    private final ReadHouseDtoMapper readHouseDtoMapper;
    private final UpdateHouseDtoMapper updateHouseDtoMapper;
    private final HousesRepository housesRepository;

    @Override
    public Response createCooperation(CreateCooperation createCooperation) {
        return null;
    }

    @Override
    public Response createHouse(CreateHouse createHouse) {
        HouseDto houseDto = createHouseDtoMapper.convertViewToDto(createHouse);
        HouseDto createdHouseDto = houseService.createHouse(houseDto);
        ReadHouse response = readHouseDtoMapper.convertDtoToView(createdHouseDto);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @Override
    public Response getCooperation(Long cooperationId) {
        return null;
    }

    @Override
    public Response getHouse(Long houseId) {
        HouseDto readHouseDto = houseService.getHouseById(houseId);
        ReadHouse houseApiResponse = readHouseDtoMapper.convertDtoToView(readHouseDto);

        return Response.ok().entity(houseApiResponse).build();
    }

    @Override
    public Response queryCooperation(@Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize) {
        return null;
    }

    @Override
    public Response queryHouse(@Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize) {
        List<ReadHouse> readHouseList = houseService.getAllHouses(pageNumber, pageSize).stream()
                .map(readHouseDtoMapper::convertDtoToView)
                .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(readHouseList).build();
    }

    @Override
    public Response removeCooperation(Long cooperationId) {
        return null;
    }

    @Override
    public Response removeHouse(Long houseId) {
        houseService.deleteById(houseId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response updateCooperation(Long cooperationId, UpdateCooperation updateCooperation) {
        return null;
    }


    @Override
    public Response updateHouse(Long houseId, UpdateHouse updateHouse) {
        HouseDto updateHouseDto = updateHouseDtoMapper.convertViewToDto(updateHouse);
        HouseDto readHouseDto = houseService.updateHouse(houseId, updateHouseDto);
        ReadHouse response = readHouseDtoMapper.convertDtoToView(readHouseDto);
        return Response.ok().entity(response).build();
    }

}
