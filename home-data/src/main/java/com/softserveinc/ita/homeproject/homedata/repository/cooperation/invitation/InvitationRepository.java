package com.softserveinc.ita.homeproject.homedata.repository.cooperation.invitation;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.invitation.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findInvitationByRegistrationToken(String token);

}
