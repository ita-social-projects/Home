package com.softserveinc.ita.homeproject.application.api;

import static com.softserveinc.ita.homeproject.application.constants.Permissions.MANAGE_IN_COOPERATION;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.api.InvitationsApi;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.ApartmentInvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationInvitationService;
import com.softserveinc.ita.homeproject.model.InvitationStatus;
import com.softserveinc.ita.homeproject.model.InvitationToken;
import com.softserveinc.ita.homeproject.model.ReadApartmentInvitation;
import com.softserveinc.ita.homeproject.model.ReadCooperationInvitation;
import com.softserveinc.ita.homeproject.model.ReadInvitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Provider
@Component
public class InvitationApiImpl extends CommonApi implements InvitationsApi {

    @Autowired
    ApartmentInvitationService apartmentInvitationService;

    @Autowired
    CooperationInvitationService cooperationInvitationService;

    @PreAuthorize(MANAGE_IN_COOPERATION)
    @Override
    public Response approveInvitation(InvitationToken token){
        ReadInvitation readInvitation = registerWithCorrectService(token.getInvitationToken());
        return Response.status(Response.Status.CREATED).entity(readInvitation).build();
    }

    private ReadInvitation registerWithCorrectService(String token){
        InvitationDto invitation = apartmentInvitationService.findInvitationByRegistrationToken(token);

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
