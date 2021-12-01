package com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface ApartmentInvitationRepository extends PagingAndSortingRepository<ApartmentInvitation, Long>,
        JpaSpecificationExecutor<ApartmentInvitation> {

    List<ApartmentInvitation> findAllBySentDatetimeIsNullAndEnabledEqualsAndStatusEquals(Boolean enabled,
                                                                                         InvitationStatus status);

    List<ApartmentInvitation> findApartmentInvitationsByEmail(String email);

}


