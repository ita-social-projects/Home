package com.softserveinc.ita.homeproject.application.api;

import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.homedata.repository.HouseRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.query.EntitySpecificationService;
import com.softserveinc.ita.homeproject.homeservice.query.QueryParamEnum;
import com.softserveinc.ita.homeproject.homeservice.query.impl.CooperationQueryConfig.CooperationQueryParamEnum;
import com.softserveinc.ita.homeproject.homeservice.query.impl.HouseQueryConfig.HouseQueryParamEnum;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import com.softserveinc.ita.homeproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.*;

@Provider
public class CooperationApiImpl extends CommonApi implements CooperationApi {

    private CooperationService cooperationService;

    private HouseService houseService;

    private HomeMapper mapper;

    private EntitySpecificationService entitySpecificationService;


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
        HouseDto toGet = houseService.getHouseById(cooperationId, houseId);
        ReadHouse readHouse = mapper.convert(toGet, ReadHouse.class);

        return Response.status(Response.Status.OK).entity(readHouse).build();
    }

    @PreAuthorize(GET_ALL_COOPERATION_PERMISSION)
    @Override
    public Response queryCooperation(@Min(1) Integer pageNumber,
                                     @Min(0) @Max(10) Integer pageSize,
                                     String sort,
                                     String filter,
                                     String name,
                                     String iban,
                                     String usreo) {

        Map<QueryParamEnum, String> filterMap = new HashMap<>();

        filterMap.put(CooperationQueryParamEnum.NAME, name);
        filterMap.put(CooperationQueryParamEnum.IBAN, iban);
        filterMap.put(CooperationQueryParamEnum.USREO, usreo);

        Page<CooperationDto> readCooperation = cooperationService.getAllCooperation(
                pageNumber,
                pageSize,
                entitySpecificationService.getSpecification(filterMap, filter, sort)
        );

        return buildQueryResponse(readCooperation, ReadCooperation.class);
    }

    @PreAuthorize(GET_HOUSES_PERMISSION)
    @Override
    public Response queryHouse(@Min(1) Long cooperationId,
                               @Min(1) Integer pageNumber,
                               @Min(0) @Max(10) Integer pageSize,
                               String sort,
                               String filter,
                               Long houseId,
                               Integer quantityFlat,
                               Integer adjoiningArea,
                               BigDecimal houseArea) {

        Map<QueryParamEnum, String> filterMap = new HashMap<>();

        String quantityFlatValue = quantityFlat == null ? null : String.valueOf(quantityFlat);
        String adjoiningAreaValue = adjoiningArea == null ? null : String.valueOf(adjoiningArea);
        String houseAreaValue = houseArea == null ? null : houseArea.toString();
        String houseIdValue = houseId == null ? null : houseId.toString();

        filterMap.put(HouseQueryParamEnum.COOPERATION_ID, cooperationId.toString());
        filterMap.put(HouseQueryParamEnum.ID, houseIdValue);
        filterMap.put(HouseQueryParamEnum.QUANTITY_FLAT, quantityFlatValue);
        filterMap.put(HouseQueryParamEnum.ADJOINING_AREA, adjoiningAreaValue);
        filterMap.put(HouseQueryParamEnum.HOUSE_AREA, houseAreaValue);

        Page<HouseDto> readHouse = houseService.getAllHouses(
                pageNumber,
                pageSize,
                entitySpecificationService.getSpecification(filterMap, filter, sort)
        );

        return buildQueryResponse(readHouse, ReadHouse.class);
    }

    @PreAuthorize(DEACTIVATE_COOPERATION_PERMISSION)
    @Override
    public Response removeCooperation(Long cooperationId) {
        cooperationService.deactivateCooperation(cooperationId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(DEACTIVATE_HOUSE_PERMISSION)
    @Override
    public Response removeHouse(Long cooperationId, Long houseId) {
        houseService.deactivateById(cooperationId, houseId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(UPDATE_COOPERATION_PERMISSION)
    @Override
    public Response updateCooperation(Long cooperationId, @Valid UpdateCooperation updateCooperation) {
        CooperationDto updateCoopDto = mapper.convert(updateCooperation, CooperationDto.class);
        CooperationDto toUpdate = cooperationService.updateCooperation(cooperationId, updateCoopDto);
        ReadCooperation readCooperation = mapper.convert(toUpdate, ReadCooperation.class);

        return Response.status(Response.Status.OK).entity(readCooperation).build();
    }

    @PreAuthorize(UPDATE_HOUSE_PERMISSION)
    @Override
    public Response updateHouse(Long cooperationId, Long houseId, @Valid UpdateHouse updateHouse) {
        HouseDto updateHouseDto = mapper.convert(updateHouse, HouseDto.class);
        HouseDto toUpdate = houseService.updateHouse(houseId, updateHouseDto);
        ReadHouse readHouse = mapper.convert(toUpdate, ReadHouse.class);

        return Response.status(Response.Status.OK).entity(readHouse).build();
    }

    @Override
    public HomeMapper getMapper() {
        return mapper;
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
    public void setSpecificationService(EntitySpecificationService entitySpecificationService) {
        this.entitySpecificationService = entitySpecificationService;
    }

}
