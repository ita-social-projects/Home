package com.softserveinc.ita.homeproject.homedata.repository;

import java.util.List;
import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CooperationInvitationRepository extends JpaRepository<CooperationInvitation, Long> {

    List<CooperationInvitation> findAllBySentDatetimeIsNullAndStatusEquals(
            InvitationStatus status);

    Optional<CooperationInvitation> findInvitationByEmail(String token);

}

