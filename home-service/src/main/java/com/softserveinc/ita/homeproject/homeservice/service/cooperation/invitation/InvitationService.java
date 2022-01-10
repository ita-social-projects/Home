package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface InvitationService<T extends Invitation, D extends InvitationDto> extends QueryableService<T, D> {

    D createInvitation(D invitationDto);

    List<D> getAllActiveInvitations();

    void markInvitationsAsOverdue();

    void updateSentDateTimeAndStatus(Long id);

    void deactivateInvitation(Long id);

    void acceptUserInvitation(T invitation);

    void registerWithRegistrationToken(String token);

    InvitationDto findInvitationByRegistrationToken(String token);

}
