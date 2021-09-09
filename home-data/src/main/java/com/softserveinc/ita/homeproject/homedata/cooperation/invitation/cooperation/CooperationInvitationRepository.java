package com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CooperationInvitationRepository extends PagingAndSortingRepository<CooperationInvitation, Long>,
        JpaSpecificationExecutor<CooperationInvitation> {

    List<CooperationInvitation> findAllBySentDatetimeIsNullAndStatusEquals(
            InvitationStatus status);

    List<CooperationInvitation> findCooperationInvitationsByEmail(String email);

}

