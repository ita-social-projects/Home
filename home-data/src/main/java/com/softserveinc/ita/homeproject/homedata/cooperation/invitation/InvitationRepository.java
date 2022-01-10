package com.softserveinc.ita.homeproject.homedata.cooperation.invitation;

import java.util.List;
import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long>,
    JpaSpecificationExecutor<Invitation> {

    Optional<Invitation> findInvitationByRegistrationToken(String token);

    List<Invitation> findAllBySentDatetimeIsNullAndStatusEqualsAndEnabledEquals(
        InvitationStatus status, Boolean enabled);

}
