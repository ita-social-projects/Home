package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.Ownership;
import com.softserveinc.ita.homeproject.homedata.entity.UserCooperation;
import com.softserveinc.ita.homeproject.homeservice.dto.UserCooperationDto;

public interface UserCooperationService extends QueryableService<UserCooperation, UserCooperationDto> {

    void createUserCooperationViaInvitation(CooperationInvitation invitation);

    void createUserCooperationForOwnership(Ownership ownership);

}
