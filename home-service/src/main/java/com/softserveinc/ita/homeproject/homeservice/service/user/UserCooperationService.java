package com.softserveinc.ita.homeproject.homeservice.service.user;

import com.softserveinc.ita.homeproject.homedata.entity.invitation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.ownership.Ownership;

public interface UserCooperationService {

    void createUserCooperationViaInvitation(CooperationInvitation invitation);

    void createUserCooperationForOwnership(Ownership ownership);

}
