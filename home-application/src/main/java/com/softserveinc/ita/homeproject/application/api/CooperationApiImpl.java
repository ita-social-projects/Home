package com.softserveinc.ita.homeproject.application.api;

import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.query.EntitySpecificationService;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import com.softserveinc.ita.homeproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.*;

@Provider
public class CooperationApiImpl extends CommonApi implements CooperationApi {

    private CooperationService cooperationService;

    private HouseService houseService;

    private HomeMapper mapper;

    private EntitySpecificationService specificationService;


    @PreAuthorize(CREATE_COOPERATION_PERMISSION)
    @Override
    public Response createCooperation(@Valid CreateCooperation createCooperation) {
        CooperationDto createCoopDto = mapper.convert(createCooperation, CooperationDto.class);
        CooperationDto readCoopDto = cooperationService.createCooperation(createCoopDto);
        ReadCooperation readCoop = mapper.convert(readCoopDto, ReadCooperation.class);

        return Response.status(Response.Status.CREATED).entity(readCoop).build();
    }

    @PreAuthorize(CREATE_HOUSE_PERMISSION)
    @Override
    public Response createHouse(Long cooperationId, @Valid CreateHouse createHouse) {
        HouseDto createHouseDto = mapper.convert(createHouse, HouseDto.class);
        HouseDto readHouseDto = houseService.createHouse(cooperationId, createHouseDto);
        ReadHouse readHouse = mapper.convert(readHouseDto, ReadHouse.class);

        return Response.status(Response.Status.CREATED).entity(readHouse).build();
    }

    @PreAuthorize(GET_COOPERATION_PERMISSION)
    @Override
    public Response getCooperation(Long cooperationId) {
        CooperationDto readCoopDto = cooperationService.getCooperationById(cooperationId);
        ReadCooperation readCoop = mapper.convert(readCoopDto, ReadCooperation.class);

        return Response.status(Response.Status.OK).entity(readCoop).build();
    }

    @PreAuthorize(GET_HOUSE_PERMISSION)
    @Override
    public Response getHouse(Long cooperationId, Long houseId) {



        return null;

//        HouseDto readHouseDto = houseService.getHouseById(houseId);
//        ReadHouse readHouse = mapper.convert(readHouseDto, ReadHouse.class);
//
//        return Response.status(Response.Status.OK).entity(readHouse).build();
    }

    @Override
    public Response queryCooperation(@Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize, String sort, String filter, String name, String iban, String usreo) {
        return null;
    }

    @Override
    public Response queryHouse(@Min(1) Integer cooperationId, @Min(1) Integer pageNumber, @Min(0) @Max(10) Integer pageSize, String sort, String filter, Integer quantityFlat, Integer adjoiningArea, String houseArea) {
        return null;
    }

    @Override
    public Response removeCooperation(Long cooperationId) {
        return null;
    }

    @Override
    public Response removeHouse(Long cooperationId, Long houseId) {
        return null;
    }

    @Override
    public Response updateCooperation(Long cooperationId, @Valid UpdateCooperation updateCooperation) {

        return null;
    }

    @Override
    public Response updateHouse(Long cooperationId, Long houseId, @Valid UpdateHouse updateHouse) {
        return null;
    }

    @Override
    public HomeMapper getMapper() {
        return null;
    }

    @Autowired
    public void setCooperationService(CooperationService cooperationService) {
        this.cooperationService = cooperationService;
    }

    @Autowired
    public void setHouseService(HouseService houseService) {
        this.houseService = houseService;
    }

    @Autowired
    public void setMapper(HomeMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setSpecificationService(EntitySpecificationService specificationService) {
        this.specificationService = specificationService;
    }
}
