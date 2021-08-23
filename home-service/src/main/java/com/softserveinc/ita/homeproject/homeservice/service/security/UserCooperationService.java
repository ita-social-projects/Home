package com.softserveinc.ita.homeproject.homeservice.service.security;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.invitation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.cooperation.ownership.Ownership;

public interface UserCooperationService {

    void createUserCooperationViaInvitation(CooperationInvitation invitation);

    void createUserCooperationForOwnership(Ownership ownership);

}
