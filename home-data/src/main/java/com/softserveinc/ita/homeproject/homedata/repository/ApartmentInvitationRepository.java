package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ApartmentInvitationRepository extends JpaRepository<ApartmentInvitation, Long> {
    List<ApartmentInvitation> findAllBySentDatetimeIsNullAndApartmentNotNullAndStatusEquals(InvitationStatus status);
    List<ApartmentInvitation> findAllByApartmentIdAndStatus(Long apartmentId, InvitationStatus status);

}

