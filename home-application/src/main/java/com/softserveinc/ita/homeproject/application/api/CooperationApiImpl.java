package com.softserveinc.ita.homeproject.application.api;

import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.homeservice.query.EntitySpecificationService;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateHouse;
import com.softserveinc.ita.homeproject.model.UpdateCooperation;
import com.softserveinc.ita.homeproject.model.UpdateHouse;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class CooperationApiImpl extends CommonApi implements CooperationApi  {

    private CooperationService cooperationService;
    private HomeMapper mapper;
    private EntitySpecificationService specificationService;


    @Override
    public Response createCooperation(@Valid CreateCooperation createCooperation) {

        return null;
    }

    @Override
    public Response createHouse(Long cooperationId, @Valid CreateHouse createHouse) {
        return null;
    }

    @Override
    public Response getCooperation(Long cooperationId) {
        return null;
    }

    @Override
    public Response getHouse(Long cooperationId, Long houseId) {
        return null;
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
}
