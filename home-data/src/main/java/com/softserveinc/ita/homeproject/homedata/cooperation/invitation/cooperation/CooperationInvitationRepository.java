package com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationType;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CooperationInvitationRepository extends PagingAndSortingRepository<CooperationInvitation, Long>,
    JpaSpecificationExecutor<CooperationInvitation> {

    List<CooperationInvitation> findAllBySentDatetimeIsNullAndStatusEqualsAndEnabledEqualsAndTypeEquals(
        InvitationStatus status, Boolean enabled, InvitationType type);

    List<CooperationInvitation> findCooperationInvitationsByEmail(String email);

}

