package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.MANAGE_COOPERATION_DATA;
import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.MANAGE_IN_COOPERATION;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.model.CreateInvitation;
import com.softserveinc.ita.homeproject.application.model.InvitationStatus;
import com.softserveinc.ita.homeproject.application.model.InvitationToken;
import com.softserveinc.ita.homeproject.application.model.InvitationType;
import com.softserveinc.ita.homeproject.application.model.ReadApartmentInvitation;
import com.softserveinc.ita.homeproject.application.model.ReadCooperationInvitation;
import com.softserveinc.ita.homeproject.application.model.ReadInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.CooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.apartment.ApartmentService;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.house.HouseService;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Provider
@Component
public class InvitationApiImpl extends CommonApi implements InvitationsApi {

    @Qualifier("invitationServiceImpl")
    @Autowired
    InvitationService<Invitation, InvitationDto> invitationService;

    @Qualifier("apartmentInvitationServiceImpl")
    @Autowired
    InvitationService<ApartmentInvitation, ApartmentInvitationDto> apartmentInvitationService;

    @Qualifier("cooperationInvitationServiceImpl")
    @Autowired
    InvitationService<CooperationInvitation, CooperationInvitationDto> cooperationInvitationService;

    @Autowired
    CooperationService cooperationService;

    @Autowired
    HouseService houseService;

    @Autowired
    ApartmentService apartmentService;

    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response approveInvitation(InvitationToken token) {
        ReadInvitation readInvitation = registerWithCorrectService(token.getInvitationToken());
        return Response.status(Response.Status.OK).entity(readInvitation).build();
    }

    @PreAuthorize(MANAGE_COOPERATION_DATA)
    @Override
    public Response createInvitation(CreateInvitation createInvitation) {
        if (createInvitation.getType().equals(InvitationType.APARTMENT)) {
            ApartmentInvitationDto invitationDto = mapper.convert(createInvitation, ApartmentInvitationDto.class);

            //TODO FIX IN ISSUE #377
            invitationDto.setId(null);

            ApartmentInvitationDto invitation = apartmentInvitationService.createInvitation(invitationDto);
            ReadApartmentInvitation readInvitation = mapper.convert(invitation, ReadApartmentInvitation.class);
            readInvitation.setType(InvitationType.APARTMENT);
            return Response.status(Response.Status.CREATED).entity(readInvitation).build();
        } else if (createInvitation.getType().equals(InvitationType.COOPERATION)) {
            CooperationInvitationDto invitationDto = mapper.convert(createInvitation, CooperationInvitationDto.class);

            //TODO FIX IN ISSUE #377
            invitationDto.setId(null);

            CooperationInvitationDto invitation = cooperationInvitationService.createInvitation(invitationDto);
            ReadCooperationInvitation readInvitation = mapper.convert(invitation, ReadCooperationInvitation.class);
            readInvitation.setType(InvitationType.COOPERATION);
            return Response.status(Response.Status.CREATED).entity(readInvitation).build();
        } else {
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }

    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response deleteAnyInvitation(Long invitationId) {
        invitationService.deactivateInvitation(invitationId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response queryAllInvitations(InvitationType type,
                                        Long cooperationId,
                                        Long apartmentId,
                                        Integer pageNumber,
                                        Integer pageSize,
                                        String sort,
                                        String filter,
                                        Long id,
                                        String email,
                                        String status) {

        Page<InvitationDto> readInvitation = invitationService
            .findAll(pageNumber, pageSize, getSpecification());

        readInvitation.stream()
                .filter(inv -> mapper.convert(inv.getType(), InvitationType.class) == InvitationType.APARTMENT)
                .forEach(inv -> inv.setAddress(houseService
                        .getOne(apartmentService
                                .getOne(apartmentInvitationService
                                        .getOne(inv.getId())
                                        .getApartmentId())
                                .getHouseId())
                        .getAddress()));

        readInvitation.stream()
                .filter(inv -> mapper.convert(inv.getType(), InvitationType.class) == InvitationType.APARTMENT)
                .forEach(inv -> inv.setHouseId(houseService
                        .getOne(apartmentService
                                .getOne(apartmentInvitationService
                                        .getOne(inv.getId())
                                        .getApartmentId())
                                .getHouseId()).getId()));

        return buildQueryResponse(readInvitation, ReadInvitation.class);
    }

    private ReadInvitation registerWithCorrectService(String token) {
        InvitationDto invitation = invitationService.findInvitationByRegistrationToken(token);

        ReadInvitation readInvitation;

        switch (invitation.getType()) {
            case APARTMENT:
                invitationService.registerWithRegistrationToken(token);
                readInvitation = mapper.convert(invitation, ReadApartmentInvitation.class);
                break;
            case COOPERATION:
                invitationService.registerWithRegistrationToken(token);
                readInvitation = mapper.convert(invitation, ReadCooperationInvitation.class);
                break;
            default:
                throw new UnsupportedOperationException(invitation.getType() + "not supported");
        }
        readInvitation.setStatus(InvitationStatus.ACCEPTED);
        return readInvitation;
    }
}
