package com.softserveinc.ita.homeproject.homeservice.service.user.ownership;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.user.ownership.Ownership;
import com.softserveinc.ita.homeproject.homeservice.dto.OwnershipDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface OwnershipService extends QueryableService<Ownership, OwnershipDto> {

    Ownership createOwnership(ApartmentInvitation apartmentInvitation);

    OwnershipDto updateOwnership(Long apartmentId, Long id, OwnershipDto updateOwnershipDto);

    void deactivateOwnershipById(Long apartmentId, Long id);
}
