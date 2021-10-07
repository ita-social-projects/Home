package com.softserveinc.ita.homeproject.homeservice.service.user;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.Ownership;

public interface UserCooperationService {

    void createUserCooperationViaInvitation(CooperationInvitation invitation);

    void createUserCooperationForOwnership(Ownership ownership);

}
