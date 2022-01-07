package com.softserveinc.ita.homeproject.homedata.cooperation.invitation.apartment;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ApartmentInvitationRepository extends PagingAndSortingRepository<ApartmentInvitation, Long>,
    JpaSpecificationExecutor<ApartmentInvitation> {

    List<ApartmentInvitation> findAllBySentDatetimeIsNullAndEnabledEqualsAndStatusEqualsAndTypeEquals(
        Boolean enabled,
        InvitationStatus status,
        InvitationType type);

    List<ApartmentInvitation> findApartmentInvitationsByEmail(String email);

}


