package com.softserveinc.ita.homeproject.homedata.cooperation.invitation;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findInvitationByRegistrationToken(String token);

}
