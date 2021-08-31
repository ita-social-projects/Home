package com.softserveinc.ita.homeproject.homeservice.service.cooperation;

import com.softserveinc.ita.homeproject.homedata.cooperation.entity.Cooperation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface CooperationService extends QueryableService<Cooperation, CooperationDto> {

    CooperationDto createCooperation(CooperationDto createCooperationDto);

    CooperationDto updateCooperation(Long id, CooperationDto updateCooperationDto);

    void deactivateCooperation(Long id);
}
