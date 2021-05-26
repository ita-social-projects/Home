package com.softserveinc.ita.homeproject.homedata.repository;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CooperationInvitationRepository extends JpaRepository<CooperationInvitation, Long> {
    List<CooperationInvitation> findAllBySentDatetimeIsNullAndCooperationNameNotNullAndStatusEquals(InvitationStatus status);
}

