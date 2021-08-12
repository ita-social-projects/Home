package com.softserveinc.ita.homeproject.homedata.repository.invitation;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.invitation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.invitation.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CooperationInvitationRepository extends JpaRepository<CooperationInvitation, Long> {

    List<CooperationInvitation> findAllBySentDatetimeIsNullAndStatusEquals(
            InvitationStatus status);

    List<CooperationInvitation> findCooperationInvitationsByEmail(String email);

}

