package com.softserveinc.ita.homeproject.application.apiservice;

import com.softserveinc.ita.homeproject.api.CooperationApiService;
import com.softserveinc.ita.homeproject.application.mapper.*;
import com.softserveinc.ita.homeproject.homedata.repository.HousesRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
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
public class CooperationApiServiceImpl implements CooperationApiService {

    private final CooperationService cooperationService;
    private final CreateCooperationDtoMapper createCooperationDtoMapper;
    private final UpdateCooperationDtoMapper updateCooperationDtoMapper;
    private final ReadCooperationDtoMapper readCooperationDtoMapper;
    private final HouseService houseService;
    private final CreateHouseDtoMapper createHouseDtoMapper;
    private final ReadHouseDtoMapper readHouseDtoMapper;
    private final UpdateHouseDtoMapper updateHouseDtoMapper;

    @Override
    public Response createCooperation(CreateCooperation createCooperation) {

        CooperationDto cooperationDto = createCooperationDtoMapper.convertViewToDto(createCooperation);
        CooperationDto readCooperationDto = cooperationService.createCooperation(cooperationDto);
        ReadCooperation readCooperation = readCooperationDtoMapper.convertDtoToView(readCooperationDto);


        return Response.status(Response.Status.CREATED).entity(readCooperation).build();
    }

    @Override
    public Response createHouse(@Min(1) Integer cooperationId, CreateHouse createHouse) {
        HouseDto houseDto = createHouseDtoMapper.convertViewToDto(createHouse);
        HouseDto createdHouseDto = houseService.createHouse(houseDto);
        ReadHouse response = readHouseDtoMapper.convertDtoToView(createdHouseDto);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }


    @Override
    public Response getCooperation(Long cooperationId) {

        CooperationDto cooperationDto = cooperationService.getCooperationById(cooperationId);
        ReadCooperation readCooperation = readCooperationDtoMapper.convertDtoToView(cooperationDto);

        return Response.status(Response.Status.OK).entity(readCooperation).build();
    }

    @Override
    public Response getHouse(Long cooperationId, Long houseId) {
        HouseDto readHouseDto = houseService.getHouseById(houseId);
        ReadHouse houseApiResponse = readHouseDtoMapper.convertDtoToView(readHouseDto);

        return Response.ok().entity(houseApiResponse).build();
    }


    @Override
    public Response queryCooperation(@Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize) {

        List<ReadCooperation> readCooperationList = cooperationService.getAllCooperation(pageNumber, pageSize).stream()
                .map(readCooperationDtoMapper::convertDtoToView)
                .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(readCooperationList).build();
    }

    @Override
    public Response queryHouse(@Min(1) Integer cooperationId, @Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize) {
        List<ReadHouse> readHouseList = houseService.getAllHouses(pageNumber, pageSize).stream()
                .map(readHouseDtoMapper::convertDtoToView)
                .collect(Collectors.toList());

        return Response.status(Response.Status.OK).entity(readHouseList).build();
    }


    @Override
    public Response removeCooperation(Long cooperationId) {

        cooperationService.deactivateCooperation(cooperationId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @Override
    public Response removeHouse(Long cooperationId, Long houseId) {
        houseService.deleteById(houseId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @Override
    public Response updateCooperation(Long cooperationId, UpdateCooperation updateCooperation) {

        CooperationDto updateCooperationDto = updateCooperationDtoMapper.convertViewToDto(updateCooperation);
        CooperationDto readCooperationDto = cooperationService.updateCooperation(cooperationId, updateCooperationDto);
        ReadCooperation readCooperation = readCooperationDtoMapper.convertDtoToView(readCooperationDto);

        return Response.status(Response.Status.OK).entity(readCooperation).build();
    }

    @Override
    public Response updateHouse(Long cooperationId, Long houseId, UpdateHouse updateHouse) {
        HouseDto updateHouseDto = updateHouseDtoMapper.convertViewToDto(updateHouse);
        HouseDto readHouseDto = houseService.updateHouse(houseId, updateHouseDto);
        ReadHouse response = readHouseDtoMapper.convertDtoToView(readHouseDto);
        return Response.ok().entity(response).build();
    }


}
