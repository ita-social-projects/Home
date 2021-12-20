package com.softserveinc.ita.homeproject.homedata.cooperation.invitation;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long>,
        JpaSpecificationExecutor<Invitation> {

    Optional<Invitation> findInvitationByRegistrationToken(String token);

}
