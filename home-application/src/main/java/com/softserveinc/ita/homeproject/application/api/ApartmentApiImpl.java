package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.MANAGE_IN_COOPERATION;
import static com.softserveinc.ita.homeproject.application.constants.Permissions.READ_APARTMENT_INFO;

import java.math.BigDecimal;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.api.ApartmentsApi;
import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.OwnershipDto;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentInvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentService;
import com.softserveinc.ita.homeproject.homeservice.service.OwnershipService;
import com.softserveinc.ita.homeproject.model.CreateApartmentInvitation;
import com.softserveinc.ita.homeproject.model.InvitationType;
import com.softserveinc.ita.homeproject.model.ReadApartmentInvitation;
import com.softserveinc.ita.homeproject.model.ReadOwnership;
import com.softserveinc.ita.homeproject.model.UpdateApartmentInvitation;
import com.softserveinc.ita.homeproject.model.UpdateOwnership;
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
    private ApartmentInvitationService invitationService;

    @Autowired
    private ApartmentService apartmentService;

    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response createInvitation(Long apartmentId,
                                     CreateApartmentInvitation createApartmentInvitation) {
        var invitationDto = mapper.convert(createApartmentInvitation, ApartmentInvitationDto.class);
        invitationDto.setApartmentId(apartmentId);
        invitationDto.setApartmentNumber(apartmentService.getOne(apartmentId).getApartmentNumber());
        var invitation = invitationService.createInvitation(invitationDto);
        var readInvitation = mapper.convert(invitation, ReadApartmentInvitation.class);
        readInvitation.setType(InvitationType.APARTMENT);
        return Response.status(Response.Status.OK).entity(readInvitation).build();
    }

    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response deleteInvitation(Long apartmentId, Long id) {
        invitationService.deactivateInvitationById(apartmentId, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response deleteOwnership(Long apartmentId, Long id) {
        ownershipService.deactivateOwnershipById(apartmentId, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PreAuthorize(MANAGE_IN_COOPERATION) // TODO don't work issue# 299 / untested method issue# 300
    @Override
    public Response getInvitation(Long apartmentId, Long id) {

        var toGet = invitationService.getOne(id, getSpecification());
        var readApartmentInvitation = mapper
                .convert(toGet, ReadApartmentInvitation.class);
        return Response.status(Response.Status.OK)
                .entity(readApartmentInvitation).build();
    }

    @PreAuthorize(READ_APARTMENT_INFO)
    @Override
    public Response getOwnership(Long apartmentId, Long id) {
        OwnershipDto toGet = ownershipService.getOne(id, getSpecification());
        var readOwnership = mapper.convert(toGet, ReadOwnership.class);

        return Response.status(Response.Status.OK).entity(readOwnership).build();
    }

    @PreAuthorize(MANAGE_IN_COOPERATION)  // TODO don't work issue# 299 / untested method issue# 300
    @Override
    public Response queryInvitation(Long apartmentId,
                                    Integer pageNumber,
                                    Integer pageSize,
                                    String sort,
                                    String filter,
                                    Long id,
                                    String email,
                                    BigDecimal ownershipPart,
                                    String status) {
        verifyExistence(apartmentId, apartmentService);
        Page<ApartmentInvitationDto> readApartmentInvitation = invitationService
                .findAll(pageNumber, pageSize, getSpecification());
        return buildQueryResponse(readApartmentInvitation, ReadApartmentInvitation.class);
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
    public Response updateInvitation(Long apartmentId,
                                     Long id,
                                     UpdateApartmentInvitation updateApartmentInvitation) {
        var updateInvitationDto = mapper.convert(updateApartmentInvitation,
                ApartmentInvitationDto.class);
        var toUpdate = invitationService.updateInvitation(apartmentId, id, updateInvitationDto);
        var readInvitation = mapper.convert(toUpdate, ReadApartmentInvitation.class);
        return Response.status(Response.Status.OK).entity(readInvitation).build();
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
