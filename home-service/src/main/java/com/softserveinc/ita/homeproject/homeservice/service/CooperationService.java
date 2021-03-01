package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface CooperationService {

    CooperationDto createCooperation(CooperationDto createCooperationDto);

    CooperationDto updateCooperation(Long id, CooperationDto updateCooperationDto);

    Page<CooperationDto> getAllCooperation(Integer pageNumber, Integer pageSize,
                                           Specification<Cooperation> specification);

    CooperationDto getCooperationById(Long id);

    void deactivateCooperation(Long id);
}
