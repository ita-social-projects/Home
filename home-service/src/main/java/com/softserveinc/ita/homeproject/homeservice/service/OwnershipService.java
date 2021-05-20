package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Ownership;
import com.softserveinc.ita.homeproject.homeservice.dto.OwnershipDto;

public interface OwnershipService extends QueryableService<Ownership, OwnershipDto> {

    OwnershipDto updateOwnership(Long apartmentId, Long id, OwnershipDto updateOwnershipDto);

    void deactivateOwnershipById(Long apartmentId, Long id);
}
