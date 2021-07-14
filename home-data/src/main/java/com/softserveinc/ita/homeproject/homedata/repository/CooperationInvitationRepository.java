package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CooperationInvitationRepository extends JpaRepository<CooperationInvitation, Long> {

    List<CooperationInvitation> findAllBySentDatetimeIsNullAndStatusEquals(
            InvitationStatus status);

    List<CooperationInvitation> findInvitationByEmail(String email);

}

