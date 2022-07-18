package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;
import com.softserveinc.ita.homeproject.homeservice.service.general.email.MailableService;


public interface InvitationService<T extends Invitation, D extends InvitationDto> extends QueryableService<T, D>,
    MailableService {

    D createInvitation(D invitationDto);

    void markInvitationsAsOverdue();

    void deactivateInvitation(Long id);

    void acceptUserInvitation(T invitation);

    void registerWithRegistrationToken(String token);

    InvitationDto findInvitationByRegistrationToken(String token);
}
