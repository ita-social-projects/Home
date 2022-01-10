package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.security.constants.Permissions.MANAGE_IN_COOPERATION;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.application.model.InvitationStatus;
import com.softserveinc.ita.homeproject.application.model.InvitationToken;
import com.softserveinc.ita.homeproject.application.model.InvitationType;
import com.softserveinc.ita.homeproject.application.model.ReadApartmentInvitation;
import com.softserveinc.ita.homeproject.application.model.ReadCooperationInvitation;
import com.softserveinc.ita.homeproject.application.model.ReadInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.CooperationService;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.apartment.ApartmentService;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.house.HouseService;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationDefaultService;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.apartment.ApartmentInvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.cooperation.CooperationInvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Provider
@Component
public class InvitationApiImpl extends CommonApi implements InvitationsApi {

    @Autowired
    ApartmentInvitationService apartmentInvitationService;

    @Autowired
    CooperationInvitationService cooperationInvitationService;

    @Autowired
    InvitationDefaultService invitationDefaultService;

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
        return Response.status(Response.Status.CREATED).entity(readInvitation).build();
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

        Page<InvitationDto> readInvitation = invitationDefaultService
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

        return buildQueryResponse(readInvitation, ReadInvitation.class);
    }

    private ReadInvitation registerWithCorrectService(String token) {
        InvitationDto invitation = apartmentInvitationService.findInvitationByRegistrationToken(token);

        ReadInvitation readInvitation;

        switch (invitation.getType()) {
            case APARTMENT:
                apartmentInvitationService.registerWithRegistrationToken(token);
                readInvitation = mapper.convert(invitation, ReadApartmentInvitation.class);
                break;
            case COOPERATION:
                cooperationInvitationService.registerWithRegistrationToken(token);
                readInvitation = mapper.convert(invitation, ReadCooperationInvitation.class);
                break;
            default:
                throw new UnsupportedOperationException(invitation.getType() + "not supported");
        }
        readInvitation.setStatus(InvitationStatus.ACCEPTED);
        return readInvitation;
    }
}
