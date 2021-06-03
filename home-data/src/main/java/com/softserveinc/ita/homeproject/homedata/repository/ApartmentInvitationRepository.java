package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface ApartmentInvitationRepository extends PagingAndSortingRepository<ApartmentInvitation, Long>,
        JpaSpecificationExecutor<ApartmentInvitation> {

    List<ApartmentInvitation> findAllBySentDatetimeIsNullAndApartmentNotNullAndStatusEquals(InvitationStatus status);

    List<ApartmentInvitation> findAllByApartmentIdAndStatus(Long apartmentId, InvitationStatus status);
}

