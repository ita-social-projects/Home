package com.softserveinc.ita.homeproject.homeservice.service.user.cooperation;

import com.softserveinc.ita.homeproject.homedata.cooperation.entity.invitation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.user.entity.ownership.Ownership;

public interface UserCooperationService {

    void createUserCooperationViaInvitation(CooperationInvitation invitation);

    void createUserCooperationForOwnership(Ownership ownership);

}
