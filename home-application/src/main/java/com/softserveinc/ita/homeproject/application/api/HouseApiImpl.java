package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.CREATE_APARTMENT_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_APARTMENTS_PERMISSION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.GET_APARTMENT_PERMISSION;

import java.math.BigDecimal;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.api.HousesApi;
import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentDto;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentService;
import com.softserveinc.ita.homeproject.homeservice.service.HouseService;
import com.softserveinc.ita.homeproject.application.model.CreateApartment;
import com.softserveinc.ita.homeproject.application.model.ReadApartment;
import com.softserveinc.ita.homeproject.application.model.UpdateApartment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Provider
@Component
public class HouseApiImpl extends CommonApi implements HousesApi {

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private HouseService houseService;

    @PreAuthorize(CREATE_APARTMENT_PERMISSION)
    @Override
    public Response createApartment(Long houseId, CreateApartment createApartment) {
        var createApartmentDto = mapper.convert(createApartment, ApartmentDto.class);
        var readApartmentDto = apartmentService.createApartment(houseId, createApartmentDto);
        var readApartment = mapper.convert(readApartmentDto, ReadApartment.class);

        return Response.status(Response.Status.CREATED).entity(readApartment).build();
    }

    @Override
    public Response deleteApartment(Long houseId, Long id) {

        apartmentService.deactivateApartment(houseId,id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(GET_APARTMENT_PERMISSION)
    @Override
    public Response getApartment(Long houseId, Long id) {
        ApartmentDto toGet = apartmentService.getOne(id, getSpecification());
        var readApartment = mapper.convert(toGet, ReadApartment.class);

        return Response.status(Response.Status.OK).entity(readApartment).build();
    }

    @PreAuthorize(GET_APARTMENTS_PERMISSION)
    @Override
    public Response queryApartment(Long houseId,
                                   Integer pageNumber,
                                   Integer pageSize,
                                   String sort,
                                   String filter,
                                   Long id,
                                   String apartmentNumber,
                                   BigDecimal apartmentArea) {
        verifyExistence(houseId, houseService);
        Page<ApartmentDto> readApartment = apartmentService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readApartment, ReadApartment.class);
    }

    @Override
    public Response updateApartment(Long houseId, Long id, UpdateApartment updateApartment) {
        var updateApartmentDto = mapper.convert(updateApartment, ApartmentDto.class);
        var toUpdate = apartmentService.updateApartment(houseId,id,updateApartmentDto);
        var readApartment = mapper.convert(toUpdate, ReadApartment.class);

        return Response.status(Response.Status.OK).entity(readApartment).build();
    }

}
