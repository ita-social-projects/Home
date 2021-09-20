package com.softserveinc.ita.homeproject.homeservice.service.user;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.user.ownership.Ownership;

public interface UserCooperationService {

    void createUserCooperationViaInvitation(CooperationInvitation invitation);

    void createUserCooperationForOwnership(Ownership ownership);

}
