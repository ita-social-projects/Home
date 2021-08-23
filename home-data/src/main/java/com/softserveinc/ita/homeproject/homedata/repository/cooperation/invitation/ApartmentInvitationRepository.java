package com.softserveinc.ita.homeproject.homedata.repository.cooperation.invitation;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.invitation.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.cooperation.invitation.InvitationStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;



public interface ApartmentInvitationRepository extends PagingAndSortingRepository<ApartmentInvitation, Long>,
        JpaSpecificationExecutor<ApartmentInvitation> {

    List<ApartmentInvitation> findAllBySentDatetimeIsNullAndStatusEquals(InvitationStatus status);

    List<ApartmentInvitation> findAllByApartmentIdAndStatus(Long apartmentId, InvitationStatus status);

    List<ApartmentInvitation> findApartmentInvitationsByEmail(String email);

}

