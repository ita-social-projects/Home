package com.softserveinc.ita.homeproject.application.api;


import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.MANAGE_IN_COOPERATION;
import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.READ_APARTMENT_INFO;

import java.math.BigDecimal;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.model.ReadOwnership;
import com.softserveinc.ita.homeproject.application.model.UpdateOwnership;
import com.softserveinc.ita.homeproject.homeservice.dto.user.ownership.OwnershipDto;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.apartment.ApartmentService;
import com.softserveinc.ita.homeproject.homeservice.service.user.ownership.OwnershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Provider
@Component
public class ApartmentApiImpl extends CommonApi implements ApartmentsApi {

    @Autowired
    private OwnershipService ownershipService;


    @Autowired
    private ApartmentService apartmentService;


    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response deleteOwnership(Long apartmentId, Long id) {
        ownershipService.deactivateOwnershipById(apartmentId, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(READ_APARTMENT_INFO)
    @Override
    public Response getOwnership(Long apartmentId, Long id) {
        OwnershipDto toGet = ownershipService.getOne(id, getSpecification());
        var readOwnership = mapper.convert(toGet, ReadOwnership.class);

        return Response.status(Response.Status.OK).entity(readOwnership).build();
    }


    @PreAuthorize(READ_APARTMENT_INFO)
    @Override
    public Response queryOwnership(Long apartmentId,
                                   Integer pageNumber,
                                   Integer pageSize,
                                   String sort,
                                   String filter,
                                   Long id,
                                   Long userId,
                                   BigDecimal ownershipPart) {
        verifyExistence(apartmentId, apartmentService);
        Page<OwnershipDto> readOwnership = ownershipService.findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readOwnership, ReadOwnership.class);
    }

    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response updateOwnership(Long apartmentId, Long id, UpdateOwnership updateOwnership) {
        var updateOwnershipDto = mapper.convert(updateOwnership, OwnershipDto.class);
        var toUpdate = ownershipService.updateOwnership(apartmentId, id, updateOwnershipDto);
        var readOwnership = mapper.convert(toUpdate, ReadOwnership.class);

        return Response.status(Response.Status.OK).entity(readOwnership).build();
    }
}
