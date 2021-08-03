package com.softserveinc.ita.homeproject.application.api;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.api.InvitationsApi;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentInvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationInvitationService;
import com.softserveinc.ita.homeproject.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Provider
@Component
public class InvitationApiImpl extends CommonApi implements InvitationsApi {

    @Autowired
    ApartmentInvitationService apartmentInvitationService;

    @Autowired
    CooperationInvitationService cooperationInvitationService;

    @Override
    public Response approveInvitation(InvitationToken token){
        ReadInvitation readInvitation = registerWithCorrectService(token.getInvitationToken());
        return Response.status(Response.Status.CREATED).entity(readInvitation).build();
    }

    private ReadInvitation registerWithCorrectService(String token){
        InvitationDto invitation = apartmentInvitationService
                .findInvitationByRegistrationToken(token);

        ReadInvitation readInvitation;

        switch (invitation.getType()){
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
