package com.softserveinc.ita.homeproject.homeservice.service.cooperation.ownership;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.invitation.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.cooperation.ownership.Ownership;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.ownership.OwnershipDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface OwnershipService extends QueryableService<Ownership, OwnershipDto> {

    Ownership createOwnership(ApartmentInvitation apartmentInvitation);

    OwnershipDto updateOwnership(Long apartmentId, Long id, OwnershipDto updateOwnershipDto);

    void deactivateOwnershipById(Long apartmentId, Long id);
}
