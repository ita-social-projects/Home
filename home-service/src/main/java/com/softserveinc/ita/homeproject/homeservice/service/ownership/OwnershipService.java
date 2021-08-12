package com.softserveinc.ita.homeproject.homeservice.service.ownership;

import com.softserveinc.ita.homeproject.homedata.entity.invitation.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.ownership.Ownership;
import com.softserveinc.ita.homeproject.homeservice.dto.ownership.OwnershipDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface OwnershipService extends QueryableService<Ownership, OwnershipDto> {

    Ownership createOwnership(ApartmentInvitation apartmentInvitation);

    OwnershipDto updateOwnership(Long apartmentId, Long id, OwnershipDto updateOwnershipDto);

    void deactivateOwnershipById(Long apartmentId, Long id);
}
